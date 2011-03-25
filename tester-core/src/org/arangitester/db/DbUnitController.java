/*
 * Copyright 2000 Universidade Federal de Minas Gerais.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arangitester.db;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.arangitester.annotations.Db;
import org.arangitester.config.ConfigDatabase;
import org.arangitester.config.ConfigDumpFile;
import org.arangitester.config.ConfigFactory;
import org.arangitester.exceptions.EnvException;
import org.arangitester.exceptions.TesterException;
import org.arangitester.util.TimerHelper;

/**
 * This class controls the database dumps, saving and populating the database tables. The goal is a frozen stable state of database to realize the 
 * test cases. Test cases usually make the database unstable, so it's difficult to replay the test to find the error again. This class recreate the 
 * stable state of the database, populating it again.<br> 
 * <b>PS:</b> You must take care when you reload a database, because all the tables exported are cleaned to be populated again.
 * @author Lucas Gonçalves
 * @author Ícaro C. F. Braga
 */
public class DbUnitController {
	private static Logger	LOG	= Logger.getLogger(DbUnitController.class);
	private static int	ACTION = 0;
	enum ActionList {export,reload}; 

	/**
	 * The constructor loads all JDBC drivers informed in user environment of tester-config.xml file.
	 */
	public DbUnitController() {
		List<ConfigDatabase> databases = ConfigFactory.getEnvSpecificConfig().getDatabases();

		if (databases != null) {
			for (ConfigDatabase database : databases) {
				try {
					Class.forName(database.getDriver());
				} catch (ClassNotFoundException e) {
					throw new TesterException("JDBC driver specified in tester-config.xml file cannot be loaded.");
				}
			}
		}
	}

	/**
	 * Execute reload/export passing parameters by terminal.
	 * @param parameters
	 *            [0] first parameter - reload/export action<br>
	 *            [1] second parameter - dump file name for a specific dump or empty for all dumps
	 * @throws Exception
	 */
	public static void main(String[] parameters) throws Exception {
		
		if (parameters.length == 0 || (!parameters[ACTION].equalsIgnoreCase("reload") && !parameters[ACTION].equalsIgnoreCase("export"))) {
			throw new EnvException("Must be informed 1 parameter at least: reload or export");
		}

		DbUnitController controller = new DbUnitController();
		ActionList action;
		if (parameters[ACTION].equals("reload")) {
			action = ActionList.reload;
		} else {
			action = ActionList.export;
		}
		List<String> files = Arrays.asList(Arrays.copyOfRange(parameters, 1, parameters.length));
		controller.verifyWitchFileToApply(files, action);
	}

	/**
	 * Get the dumpfiles that was informed in annotation @Db in a specific class and execute them. If there aren't a 
	 * annotation @Db in the class, nothing is done.  
	 * @param clazz
	 *            Test Case Class
	 */
	public void reload(Class<?> clazz) {
		Db db = clazz.getAnnotation(Db.class);
		if (db != null) {
			verifyWitchFileToApply(Arrays.asList(db.value()), ActionList.reload);
		}
	}

	private void verifyWitchFileToApply(List<String> dumpFileName, ActionList action) {
		List<ConfigDatabase> databases = ConfigFactory.getEnvSpecificConfig().getDatabases();
		
		for (ConfigDatabase database : databases) {
			boolean databaseReloaded = false;
			for (ConfigDumpFile dumpFile : database.getFile()) 
			{
				if (dumpFileName.contains(dumpFile.getName())) 
				{
					switch(action)
					{
						case export:
							export(database, dumpFile);
							break;
						case reload:
							databaseReloaded = true;
							reload(database, dumpFile);
							break;	
					}
				}
			}
			if (databaseReloaded) {
				this.executeListener(database);
			}
		}
	}

	private void executeListener(ConfigDatabase database) {
		if (StringUtils.isNotBlank(database.getListener())) {
			try {
				Class<?> listenerClass = Class.forName(database.getListener());
				IReloadListener newInstance = (IReloadListener) listenerClass.newInstance();
				newInstance.reload(database);
			} catch (Exception e) {
				throw new EnvException(String.format("Error executing listener %s", database.getListener()), e);
			}
		}
	}

	private void export(ConfigDatabase database, ConfigDumpFile dumpFile) {
		TimerHelper timer = new TimerHelper();
		LOG.info(String.format("Exporting schema %s: %s.xml",dumpFile.getName(),dumpFile.getName()));
		try {
			loadInternalDriver(database).export(database, dumpFile);
			LOG.info("Schema exported successfully!");
			LOG.info(String.format("Tempo gasto: %dmin %dseg",timer.getMin(),timer.getSec()));
		} catch (Exception e) {
			throw new EnvException(String.format("database '%s' cannot be exported : %s.xml",dumpFile.getName(),dumpFile.getName()), e);
		}
	}

	private void reload(ConfigDatabase database, ConfigDumpFile schema) {
		TimerHelper timer = new TimerHelper();
		LOG.info(String.format("Reloading schema %s: %s.xml",schema.getName(),schema.getName()));
		try {
			loadInternalDriver(database).reload(database, schema);
			LOG.info("Esquema recarregado com sucesso!");
			LOG.info("Tempo gasto: " + timer.getMin() + "min " + timer.getSec() + "seg");
		} catch (Exception e) {
			throw new EnvException(String.format("database '%s' cannot be loaded : %s.xml",schema.getName(),schema.getName()), e);
		}
	}
	
	private DriverDb loadInternalDriver(ConfigDatabase database)
	{
		// TODO Take off the dependence of 'if's. This configuration must be in a config.xml file.
		DriverDb db = null;
		if (database.getDriver().contains("oracle")) {
			db = new OracleDb();
		} else if (database.getDriver().contains("postgres")) {
			db = new PostgresDb();
		} else if (database.getDriver().contains("mysql")) {
			db = new MysqlDb();
		} else if (database.getDriver().contains("sqlserver")) {
			db = new SqlServerDb();
		}
		return db;
	}
}
