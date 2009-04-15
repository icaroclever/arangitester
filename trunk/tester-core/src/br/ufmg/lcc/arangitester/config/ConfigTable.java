package br.ufmg.lcc.arangitester.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("table")
public class ConfigTable {
	
	@XStreamAsAttribute
	private boolean not;
	
	@XStreamAsAttribute
	private String name;

	public boolean isNot() {
		return not;
	}

	public void setNot(boolean not) {
		this.not = not;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
