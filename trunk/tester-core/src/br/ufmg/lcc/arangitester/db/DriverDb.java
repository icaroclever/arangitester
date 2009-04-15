package br.ufmg.lcc.arangitester.db;

import br.ufmg.lcc.arangitester.config.ConfigDatabase;
import br.ufmg.lcc.arangitester.config.ConfigDumpFile;

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
