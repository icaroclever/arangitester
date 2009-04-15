package br.ufmg.lcc.arangitester.config;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author Lucas Gonçalves
 * 
 */

@XStreamAlias("database")
public class ConfigDatabase {
	private String driver;
	private String user;
	private String password;
	private String url;

	@XStreamImplicit
	private List<ConfigDumpFile> file;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ConfigDumpFile> getFile() {
		return file;
	}

	public void setFiles(List<ConfigDumpFile> files) {
		this.file = files;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
