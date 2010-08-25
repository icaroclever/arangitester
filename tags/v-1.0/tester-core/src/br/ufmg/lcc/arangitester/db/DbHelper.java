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

import org.apache.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.filter.DefaultTableFilter;

import br.ufmg.lcc.arangitester.config.ConfigDumpFile;
import br.ufmg.lcc.arangitester.config.ConfigTable;

/**
 * @author Lucas Gonçalves
 * 
 */
public class DbHelper {
	
	
	public static void printStatistics(IDataSet filteredDs) {
		Logger log = Logger.getLogger("DB_EXPORT");
		try {
			int total = 0;
			for (String name: filteredDs.getTableNames()) {
				ITable table = filteredDs.getTable(name);
				int rowCount = table.getRowCount();
				total += rowCount;
				if (rowCount > 100) {
					log.warn(String.format("%s: %s",name ,rowCount ));
				} else {
					log.debug(String.format("%s: %s",name ,rowCount ));
				}
			}
			log.info("Total Lines: " + total);
		} catch (Exception e) {
			log.error("Printing statistics");
		}
	}
	
	public static DefaultTableFilter getIncludeExcludeFilter(ConfigDumpFile dumpFileConfig) {
		DefaultTableFilter filter = new DefaultTableFilter();
		if (dumpFileConfig.getTables()!= null) {
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
