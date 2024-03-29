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

import org.arangitester.config.ConfigDatabase;
import org.arangitester.config.ConfigDumpFile;

/**
 * Representa um tipo de banco de dados. Postgres, mysql, oracle, generic... Permite fazer ajustes dependendo do banco.
 * 
 * @author Lucas Gonçalves
 * 
 */
public interface DriverDb {

	/**
	 * Exporta um schema de banco de dados.
	 * 
	 * @param database
	 *            Configurações para acesso ao banco de dados.
	 * @param schema
	 *            a ser exportado.
	 * @throws Exception
	 *             Genérico.
	 */
	public void export(ConfigDatabase database, ConfigDumpFile schema) throws Exception;

	/**
	 * Reload um schema de banco de dados.
	 * 
	 * @param database
	 *            Configurações para acesso ao banco de dados.
	 * @param schema
	 *            configurações do arquivo tester-config.xml que indicam qual schema será reloaded.
	 * @throws Exception
	 *             Genérico.
	 */
	public void reload(ConfigDatabase database, ConfigDumpFile schema) throws Exception;
}
