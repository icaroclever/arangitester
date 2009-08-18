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
import org.dbunit.ext.mssql.InsertIdentityOperation;

import br.ufmg.lcc.arangitester.config.ConfigDatabase;
import br.ufmg.lcc.arangitester.config.ConfigDumpFile;

/**
 * @author goncalvesl
 *
 */
public class SqlServerDb implements DriverDb {

	@Override
	public void export(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, schema.getQualifiedTableName());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, schema.getCaseSensetive());

		IDataSet dataset = connection.createDataSet();
		IDataSet filteredDs = new FilteredDataSet(DbHelper.getIncludeExcludeFilter(schema), dataset);

		XmlDataSet.write(filteredDs, new FileOutputStream(schema.getName() + ".xml"));

		connection.close();

	}

	@Override
	public void reload(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, schema.getQualifiedTableName());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, schema.getCaseSensetive());
		IDataSet ds = new XmlDataSet(new FileInputStream(schema.getName() + ".xml"));
		
		InsertIdentityOperation.CLEAN_INSERT.execute(connection, ds);

		connection.close();
	}

}
