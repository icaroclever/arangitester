package br.ufmg.lcc.arangitester.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("environment")
public class ConfigEnv {
	
	@XStreamAsAttribute
	private String name;
	
	private String path;
	private String host;
	private int port;
	private String browser;
	
	@XStreamImplicit
	private List<ConfigDatabase> databases;
	
	public String getPath() {
		if (!path.startsWith("/")){
			path = "/" + path;
		}
		if (path.endsWith("/"))
			path = path.substring(0, path.length()-1);
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ConfigDatabase> getDatabases() {
		return databases;
	}

	public void setDatabases(List<ConfigDatabase> databases) {
		this.databases = databases;
	}

}
