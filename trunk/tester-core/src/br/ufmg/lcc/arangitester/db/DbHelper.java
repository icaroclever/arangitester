package br.ufmg.lcc.arangitester.db;

import org.dbunit.dataset.filter.DefaultTableFilter;

import br.ufmg.lcc.arangitester.config.ConfigDumpFile;
import br.ufmg.lcc.arangitester.config.ConfigTable;

/**
 * @author Lucas Gonçalves
 * 
 */
public class DbHelper {
	
	public static DefaultTableFilter getIncludeExcludeFilter(ConfigDumpFile dumpFileConfig) {
		DefaultTableFilter filter = new DefaultTableFilter();
		if (dumpFileConfig.getNotTablesNames().length != 0) {
			for(ConfigTable table: dumpFileConfig.getTables()) {
				if (table.isNot()) {
					filter.excludeTable(table.getName());
				} else {
					filter.includeTable(table.getName());
				}
			}
		}		
		return filter;
	}
	
}
