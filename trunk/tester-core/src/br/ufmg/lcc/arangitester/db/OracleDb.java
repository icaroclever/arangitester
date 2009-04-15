package br.ufmg.lcc.arangitester.db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
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
	@Override
	public void export(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new OracleConnection(jdbcConnection, schema.getName().toUpperCase());
		connection.getConfig().setFeature(DatabaseConfig.FEATURE_SKIP_ORACLE_RECYCLEBIN_TABLES, true);

		IDataSet dataset = connection.createDataSet();
		IDataSet filteredDs = new FilteredDataSet(DbHelper.getIncludeExcludeFilter(schema), dataset);

		Properties prop = new Properties();
		HashMap<String, Long> sequences = getSequences(connection, schema);
		for (String key : sequences.keySet()) {
			prop.setProperty(key, String.valueOf(sequences.get(key)));
		}

		prop.store(new FileWriter("sequences.txt"), "Sequences");

		ITableFilter sequence = new DatabaseSequenceFilter(connection);
		IDataSet fullDataSet = new FilteredDataSet(sequence, filteredDs);

		XmlDataSet.write(fullDataSet, new FileOutputStream(schema.getName() + ".xml"));

		connection.close();
	}

	/**
	 * Reload the oracle database, including sequences. It loads the file sequences.txt wich is a property file that must contain all the last_number of all sequences (check the analytical table
	 * all_sequences: select * from all_sequences). <br>
	 * <br>
	 * It assumes that all the sequences have the following characteristics: <br>
	 * INVREMENT_BY=1 <br>
	 * MIN_VALUE=1 <br>
	 * <br>
	 * It changes the MIN_VALUE to 0.
	 * 
	 * 
	 * Parte do pressuposto de que as sequencias tem os seguintes atributos setados como: INCREMENT_BY=1 MIN_VALUE=1 E necessario refatorar este metodo para que ele fique mais generico. O ideal eh q o
	 * arquivo sequences.txt contivesse todos os atributos das sequencias, para que eles fossem recuperados a partir deste metodo.
	 */
	@Override
	public void reload(ConfigDatabase database, ConfigDumpFile schema) throws Exception {
		Connection jdbcConnection = DriverManager.getConnection(database.getUrl(), database.getUser(), database.getPassword());
		IDatabaseConnection connection = new OracleConnection(jdbcConnection, schema.getName().toUpperCase());

		Properties prop = new Properties();
		FileReader file = new FileReader("sequences.txt");
		if (file == null)
			throw new Exception("File sequences.txt not found.");

		prop.load(file);

		HashMap<String, Long> sequences = getSequences(connection, schema);
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

		IDataSet ds = new XmlDataSet(new FileInputStream(schema.getName() + ".xml"));
		DatabaseOperation.CLEAN_INSERT.execute(connection, ds);

		connection.close();
	}

	/**
	 * Export the last value of the sequences
	 */
	private HashMap<String, Long> getSequences(IDatabaseConnection connection, ConfigDumpFile schema) throws Exception {
		HashMap<String, Long> map = new HashMap<String, Long>();
		ResultSet seqResult = connection.getConnection().createStatement().executeQuery("select SEQUENCE_NAME, LAST_NUMBER from all_sequences where SEQUENCE_OWNER = '" + schema.getName() + "'");
		while (seqResult.next()) {
			String name = seqResult.getString(1);
			Long lastValue = seqResult.getLong(2);
			map.put(name, lastValue);
		}
		return map;
	}

}
