package br.ufmg.lcc.arangitester.db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

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
public class MysqlDb implements DriverDb {

	@Override
	public void export(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		// connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, true);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, true);

		IDataSet dataset = connection.createDataSet();
		IDataSet filteredDs = new FilteredDataSet(DbHelper.getIncludeExcludeFilter(schema), dataset);

		XmlDataSet.write(filteredDs, new FileOutputStream(schema.getName() + ".xml"));

		connection.close();
	}

	@Override
	public void reload(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, schema.getCaseSensetive());
		// Saves the current state of the foreign_key_checks
		ResultSet foreignKeyCheck = connection.getConnection().prepareStatement("select @@FOREIGN_KEY_CHECKS").executeQuery();
		foreignKeyCheck.first();
		int previous_constraint_state = foreignKeyCheck.getInt(1);

		connection.getConnection().prepareStatement("set FOREIGN_KEY_CHECKS=0").execute();
		IDataSet ds = new XmlDataSet(new FileInputStream(schema.getName() + ".xml"));

		// ITableFilter filter = new DatabaseSequenceFilter(connection, ds.getTableNames());
		// IDataSet fullDataSet = new FilteredDataSet(filter, ds);

		// DatabaseOperation.CLEAN_INSERT.execute(connection, fullDataSet);

		DatabaseOperation.CLEAN_INSERT.execute(connection, ds);

		connection.getConnection().prepareStatement("set FOREIGN_KEY_CHECKS=" + previous_constraint_state).execute();
		connection.close();
	}

}
