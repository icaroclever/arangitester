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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.annotations.Db;
import br.ufmg.lcc.arangitester.config.ConfigDatabase;
import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.config.ConfigDumpFile;
import br.ufmg.lcc.arangitester.exceptions.EnvException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.util.TimerHelper;

/**
 * Class responsible to control database dump.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class DbUnitController {
	private static Logger LOG = Logger.getLogger(DbUnitController.class);

	/**
	 * Carrega todos os drivers de todos os bancos de dados configurados no arquivo tester-config.xml
	 */
	public DbUnitController() {
		List<ConfigDatabase> databases = ConfigFactory.getEnvSpecificConfig().getDatabases();

		if (databases != null) {
			for (ConfigDatabase database : databases) {
				try {
					Class.forName(database.getDriver());
				} catch (ClassNotFoundException e) {
					throw new TesterException("A classe de controle do banco de dados especificada no arquivo tester-config.xml não existe");
				}
			}
		}
	}

	/**
	 * Permite fazer o reload/export fora de uma bateria de testes.
	 * 
	 * @param args
	 *            [0] Ação reload/export <br>
	 *            [1] Nome do arquivo de dump ou nulo para reload de todos.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0 || (!args[0].equals("reload") && !args[0].equals("export"))) {
			throw new EnvException("Deve ser passado pelo menos 1 parametro: reload ou export");
		}

		DbUnitController controller = new DbUnitController();
		if (args[0].equals("reload")) {
			if (args.length == 1)
				controller.verifyWitchFileToApply((String[]) null, false);
			else {
				String[] files = Arrays.copyOfRange(args, 1, args.length);
				controller.verifyWitchFileToApply(files, false);
			}
		} else {
			if (args.length == 1)
				controller.verifyWitchFileToApply((String[]) null, true);
			else {
				String[] files = Arrays.copyOfRange(args, 1, args.length);
				controller.verifyWitchFileToApply(files, true);
			}
		}

	}

	/**
	 * Apartir de uma classe de teste pega quais arquivos de dump precisão ser reloaded.
	 * 
	 * @param clazz
	 *            Classe de teste.
	 */
	public void reload(Class<?> clazz) {
		Db lccDb = clazz.getAnnotation(Db.class);
		if (lccDb == null) {
			verifyWitchFileToApply((String[]) null, false);
		} else {
			verifyWitchFileToApply(lccDb.value(), false);
		}
	}

	public void verifyWitchFileToApply(String[] dumpFileName, boolean export) {
		List<ConfigDatabase> databases = ConfigFactory.getEnvSpecificConfig().getDatabases();
		if (dumpFileName == null || dumpFileName.length == 0) { // Reload todas os arquivos de dump configurados no arquivo tester-config.xml
			if (databases != null) {
				for (ConfigDatabase database : databases) {
				    boolean databaseReloaded = false;
				    for (ConfigDumpFile dumpFile : database.getFile()) {
						if (export) {
							export(database, dumpFile);
						} else {
						    databaseReloaded = true;
							reload(database, dumpFile);
						}
					}
					if (databaseReloaded) {
                        this.executeListener(database);
                    }
				}
			}
		} else {
			for (String toLoad : dumpFileName) { // Reload APENAS os arquivos de dump passados na linha de comando.
				for (ConfigDatabase database : databases) {
				    boolean databaseReloaded = false;
					for (ConfigDumpFile dumpFile : database.getFile()) {
						if (dumpFile.getName().equals(toLoad) || dumpFile.getName().equals(toLoad)) {
							if (export) {
								export(database, dumpFile);
							} else {
							    databaseReloaded = true;
								reload(database, dumpFile);
							}
							break;
						}
					}
					if (databaseReloaded) {
					    this.executeListener(database);
					}
				}
			}
		}
	}

	private void executeListener(ConfigDatabase database) {
        if (StringUtils.isNotBlank(database.getListener())) {
            try {
                Class< ? > listenerClass = Class.forName(database.getListener());
                IReloadListener newInstance = (IReloadListener) listenerClass.newInstance();
                newInstance.reload(database);
            } catch (Exception e) {
                throw new EnvException(String.format("Error executing listener %s", database.getListener()), e);
            }
        }
	}
	
	private void export(ConfigDatabase database, ConfigDumpFile dumpFile) {
		TimerHelper timer = new TimerHelper();
		LOG.info("Exportando schema " + dumpFile.getName() + ":" + dumpFile.getName() + ".xml");
		try {
			DriverDb db = null;
			if (database.getDriver().contains("oracle")) {
				db = new OracleDb();
			} else if (database.getDriver().contains("postgres")) {
				db = new PostgresDb();
			} else if (database.getDriver().contains("mysql")) {
				db = new MysqlDb();
			}
			db.export(database, dumpFile);
			LOG.info("Esquema exportado com sucesso!");
			LOG.info("Tempo gasto: " + timer.getMin() + "min " + timer.getSec() + "seg");
		} catch (Exception e) {
			throw new EnvException("Error ao tentar exportar o banco " + dumpFile.getName() + " : " + dumpFile.getName() + ".xml", e);
		}
	}

	private void reload(ConfigDatabase database, ConfigDumpFile schema) {
		TimerHelper timer = new TimerHelper();
		LOG.info("Recarregando schema " + schema.getName() + ":" + schema.getName() + ".xml");
		try {
			DriverDb db = null;
			if (database.getDriver().contains("oracle")) {
				db = new OracleDb();
			} else if (database.getDriver().contains("postgres")) {
				db = new PostgresDb();
			} else if (database.getDriver().contains("mysql")) {
				db = new MysqlDb();
			}
			db.reload(database, schema);
			LOG.info("Esquema recarregado com sucesso!");
			LOG.info("Tempo gasto: " + timer.getMin() + "min " + timer.getSec() + "seg");
		} catch (Exception e) {
			throw new EnvException("Error ao tentar recarregar o banco " + schema.getName() + " : " + schema.getName() + ".xml", e);
		}
	}

}
