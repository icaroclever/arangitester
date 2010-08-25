package br.ufmg.lcc.arangitester.db;

import br.ufmg.lcc.arangitester.config.ConfigDatabase;

/**
 * @author Lucas Gonçalves
 * 
 */
public interface IReloadListener {
	void reload(ConfigDatabase database);
}
