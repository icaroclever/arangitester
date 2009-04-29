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
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import br.ufmg.lcc.arangitester.config.ConfigDatabase;
import br.ufmg.lcc.arangitester.config.ConfigDumpFile;

/**
 * @author Lucas Gonçalves
 * 
 */
public class PostgresDb implements DriverDb {

	@Override
	public void export(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection, schema.getSchema());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, schema.getQualifiedTableName());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, schema.getCaseSensetive());

		IDataSet dataset = connection.createDataSet();

		// ITableFilter filter = new DatabaseSequenceFilter(connection);
		IDataSet fullDataSet = new FilteredDataSet(DbHelper.getIncludeExcludeFilter(schema), dataset);

		XmlDataSet.write(fullDataSet, new FileOutputStream(schema.getName() + ".xml"));

		connection.close();
	}

	@Override
	public void reload(ConfigDatabase database, ConfigDumpFile schema) throws Exception {

		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection, schema.getSchema());

		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, schema.getCaseSensetive());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, schema.getQualifiedTableName());
		jdbcConnection.setAutoCommit(false);
		jdbcConnection.createStatement().execute("update pg_constraint set condeferrable = 't' where contype = 'f';");
		jdbcConnection.createStatement().execute("update pg_trigger set tgdeferrable=true where tgisconstraint = true;");
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		
		IDataSet ds = new XmlDataSet(new FileInputStream(schema.getName() + ".xml"));

		// ITableFilter filter = new DatabaseSequenceFilter(connection);
		// IDataSet fullDataSet = new FilteredDataSet(filter, ds);
		
		DatabaseOperation.DELETE_ALL.execute(connection, ds);
		jdbcConnection.commit();
		
		jdbcConnection.createStatement().execute("update pg_constraint set condeferrable = 't' where contype = 'f';");
		jdbcConnection.createStatement().execute("update pg_trigger set tgdeferrable=true where tgisconstraint = true;");
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		DatabaseOperation.INSERT.execute(connection, ds);
		jdbcConnection.commit();
		connection.close();
	}

}
