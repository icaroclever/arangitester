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
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);

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

		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
		jdbcConnection.setAutoCommit(false);
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		
		IDataSet ds = new XmlDataSet(new FileInputStream(schema.getName() + ".xml"));

		// ITableFilter filter = new DatabaseSequenceFilter(connection);
		// IDataSet fullDataSet = new FilteredDataSet(filter, ds);
		
		DatabaseOperation.DELETE_ALL.execute(connection, ds);
		jdbcConnection.commit();
		
		jdbcConnection.createStatement().execute("SET CONSTRAINTS ALL DEFERRED");
		DatabaseOperation.INSERT.execute(connection, ds);
		jdbcConnection.commit();
		connection.close();
	}

}
