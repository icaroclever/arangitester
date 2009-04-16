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
