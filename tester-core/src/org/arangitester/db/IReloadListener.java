package org.arangitester.db;

import org.arangitester.config.ConfigDatabase;

/**
 * @author Lucas Gonçalves
 * 
 */
public interface IReloadListener {
	void reload(ConfigDatabase database);
}
