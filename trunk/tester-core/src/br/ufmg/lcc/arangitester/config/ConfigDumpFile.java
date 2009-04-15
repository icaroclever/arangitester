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
	private String name;

	@XStreamAsAttribute
	@XStreamAlias("casesensetive")
	private Boolean caseSensetive;

	@XStreamAsAttribute
	private String schema;

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
	private List<ConfigTable> tables;

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
