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
package br.ufmg.lcc.arangitester.config;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author Lucas Gonçalves
 * 
 */
@XStreamAlias("file")
public class ConfigDumpFile {
	@XStreamAsAttribute
	private String	name;

	@XStreamAsAttribute
	@XStreamAlias("casesensetive")
	private Boolean	caseSensetive;

	@XStreamAsAttribute
	@XStreamAlias("qualifiedtablename")
	private Boolean	qualifiedTableName;

	@XStreamAsAttribute
	private String	schema;

	@XStreamAsAttribute
	@XStreamAlias("schemasequences")
	private String	schemasequences;

	public Boolean getQualifiedTableName() {
		if (this.qualifiedTableName == null) {
			return false;
		}
		return qualifiedTableName;
	}

	public void setQualifiedTableName(Boolean qualifiedTableName) {
		this.qualifiedTableName = qualifiedTableName;
	}

	public String getSchemasequences() {
		if (this.schemasequences == null) {
			return "";
		}
		return schemasequences;
	}

	public void setSchemasequences(String schemasequences) {
		this.schemasequences = schemasequences;
	}

	public Boolean getCaseSensetive() {
		if (this.caseSensetive == null) {
			return false;
		}
		return caseSensetive;
	}

	public void setCaseSensetive(Boolean caseSensetive) {
		this.caseSensetive = caseSensetive;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	@XStreamImplicit
	private List<ConfigTable>	tables;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getNotTablesNames() {
		List<String> nots = new ArrayList<String>();
		if (tables != null) {
			for (ConfigTable table : tables) {
				if (table.isNot()) {
					nots.add(table.getName());
				}
			}
		}

		return nots.toArray(new String[0]);
	}

	public String[] getTablesNames() {
		List<String> in = new ArrayList<String>();
		if (tables != null) {
			for (ConfigTable table : tables) {
				if (!table.isNot()) {
					in.add(table.getName());
				}
			}
		}

		return in.toArray(new String[0]);
	}

	public List<ConfigTable> getTables() {
		return tables;
	}

	public void setTables(List<ConfigTable> tables) {
		this.tables = tables;
	}
}
