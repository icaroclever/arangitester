package org.arangitester.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.arangitester.config.ConfigDatabase;
import org.arangitester.config.ConfigDumpFile;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.mssql.InsertIdentityOperation;


/**
 * @author goncalvesl
 * 
 */
public class SqlServerDb implements DriverDb {
	Logger	log	= Logger.getLogger("DB_EXPORT");

	public void export(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, schema.getQualifiedTableName());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, schema.getCaseSensetive());

		IDataSet dataset = connection.createDataSet();
		IDataSet filteredDs = new FilteredDataSet(DbHelper.getIncludeExcludeFilter(schema), dataset);
		DbHelper.printStatistics(filteredDs);

		XmlDataSet.write(filteredDs, new FileOutputStream(schema.getName() + ".xml"));
		long length = new File(schema.getName() + ".xml").length();
		double mb = (double) length / (double)1048576;
		log.info("File Size: " + new DecimalFormat("0.0").format(mb) + "MB");
		connection.close();
	}

	public void reload(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, schema.getQualifiedTableName());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_CASE_SENSITIVE_TABLE_NAMES, schema.getCaseSensetive());
		IDataSet ds = new XmlDataSet(new FileInputStream(schema.getName() + ".xml"));
		jdbcConnection.setAutoCommit(false);
		jdbcConnection.createStatement().execute("exec sp_MSforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'");
		jdbcConnection.createStatement().execute("exec sp_MSforeachtable 'ALTER TABLE ? DISABLE TRIGGER ALL'");

		InsertIdentityOperation.CLEAN_INSERT.execute(connection, ds);

		jdbcConnection.createStatement().execute("exec sp_MSforeachtable 'ALTER TABLE ? CHECK CONSTRAINT ALL'");
		//jdbcConnection.createStatement().execute("exec sp_MSforeachtable 'ALTER TABLE ? ENABLE TRIGGER ALL'");

		jdbcConnection.commit();
		connection.close();
	}

}
