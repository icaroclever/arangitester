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
package br.ufmg.lcc.arangitester.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.oracle.OracleConnection;
import org.dbunit.operation.DatabaseOperation;

import br.ufmg.lcc.arangitester.config.ConfigDatabase;
import br.ufmg.lcc.arangitester.config.ConfigDumpFile;

/**
 * @author Lucas Gonçalves
 * 
 */
public class OracleDb implements DriverDb {

	/**
	 * 
	 */
	public void export(ConfigDatabase database, ConfigDumpFile fileConfig) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		String schemaName = fileConfig.getSchema();
		if (schemaName != null) {
			schemaName = schemaName.toUpperCase();
		}
		IDatabaseConnection connection = new OracleConnection(jdbcConnection, schemaName);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, true);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, fileConfig.getQualifiedTableName());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, fileConfig.getCaseSensetive());

		IDataSet dataset = connection.createDataSet();
		IDataSet filteredDs = new FilteredDataSet(DbHelper.getIncludeExcludeFilter(fileConfig), dataset);

		Properties prop = new Properties();
		HashMap<String, Long> sequences = getSequences(database, connection, fileConfig);
		for (String key : sequences.keySet()) {
			prop.setProperty(key, String.valueOf(sequences.get(key)));
		}

		prop.store(new FileWriter(fileConfig.getName() + "_sequences.txt"), "Sequences");
		DbHelper.printStatistics(filteredDs);
		XmlDataSet.write(filteredDs, new FileOutputStream(fileConfig.getName() + ".xml"));

		connection.close();
	}

	/**
	 * Reload the oracle database, including sequences. It loads the file sequences.txt wich is a property file that must contain all the last_number of all sequences (check the
	 * analytical table
	 * all_sequences: select * from all_sequences). <br>
	 * <br>
	 * It assumes that all the sequences have the following characteristics: <br>
	 * INVREMENT_BY=1 <br>
	 * MIN_VALUE=1 <br>
	 * <br>
	 * It changes the MIN_VALUE to 0.
	 * 
	 * 
	 * Parte do pressuposto de que as sequencias tem os seguintes atributos setados como: INCREMENT_BY=1 MIN_VALUE=1 E necessario refatorar este metodo para que ele fique mais
	 * generico. O ideal eh q o
	 * arquivo sequences.txt contivesse todos os atributos das sequencias, para que eles fossem recuperados a partir deste metodo.
	 */
	public void reload(ConfigDatabase database, ConfigDumpFile fileConfig) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		String schemaName = fileConfig.getSchema();
		if (schemaName != null) {
			schemaName = schemaName.toUpperCase();
		}
		IDatabaseConnection connection = new OracleConnection(jdbcConnection, schemaName);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, fileConfig.getQualifiedTableName());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, fileConfig.getCaseSensetive());

		Properties prop = new Properties();
		try{
			FileReader file = new FileReader(fileConfig.getName() + "_sequences.txt");
			prop.load(file);
		}catch(FileNotFoundException fnfe)
		{
			throw new Exception("File sequences.txt not found.\n"+fnfe.getMessage());
		}
		

		HashMap<String, Long> sequences = getSequences(database, connection, fileConfig);
		Enumeration<Object> keys = prop.keys();
		while (keys.hasMoreElements()) {
			String name = (String) keys.nextElement();
			long lastValueNeeded = Long.valueOf(prop.getProperty(name));
			long lastValueCurrent = sequences.get(name);
			connection.getConnection().createStatement().execute("ALTER SEQUENCE " + name + " NOCACHE MINVALUE 0");
			long newIncrease = lastValueNeeded - lastValueCurrent;
			if (newIncrease != 0) {
				connection.getConnection().createStatement().execute("ALTER SEQUENCE " + name + " INCREMENT BY " + newIncrease);
				connection.getConnection().createStatement().execute("SELECT " + name + ".NEXTVAL FROM dual");
				connection.getConnection().createStatement().execute("ALTER SEQUENCE " + name + " INCREMENT BY 1");
			}
		}

		IDataSet ds = new XmlDataSet(new FileInputStream(fileConfig.getName() + ".xml"));
		jdbcConnection.setAutoCommit(false);
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		DatabaseOperation.CLEAN_INSERT.execute(connection, ds);

		connection.close();
	}

	/**
	 * Export the last value of the sequences
	 */
	private HashMap<String, Long> getSequences(ConfigDatabase database, IDatabaseConnection connection, ConfigDumpFile file) throws Exception {
		HashMap<String, Long> map = new HashMap<String, Long>();
		for (String schema : StringUtils.split(file.getSchemasequences(), ",")) {
			ResultSet seqResult = connection.getConnection().createStatement().executeQuery(
					"select SEQUENCE_NAME, LAST_NUMBER from all_sequences where SEQUENCE_OWNER = '" + schema + "'");
			while (seqResult.next()) {
				String name = schema + "." + seqResult.getString(1);
				Long lastValue = seqResult.getLong(2);
				map.put(name, lastValue);
			}
		}

		return map;
	}

}
