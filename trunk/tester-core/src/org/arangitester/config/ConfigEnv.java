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
package org.arangitester.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("environment")
public class ConfigEnv {

	@XStreamAsAttribute
	private String					name;

	private String					path;
	private String					host;
	private int						port;
	private String					browser;

	@XStreamAlias("firefox-profile-dir")
	private String					firefoxProfile;

	@XStreamAlias("selenium-proxy-port")
	private String					seleniumServerPort;

	@XStreamImplicit
	private List<ConfigDatabase>	databases;

	public String getPath() {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		if (path.endsWith("/"))
			path = path.substring(0, path.length() - 1);
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHost() {
		String extraHost = System.getProperty("app.host");
		if (StringUtils.isNotBlank(extraHost)) {
			return extraHost;
		}
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		String extraPortStr = System.getProperty("app.port");
		if (StringUtils.isNotBlank(extraPortStr)) {
			return Integer.valueOf(extraPortStr);
		}
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

	public void setFirefoxProfile(String firefoxProfile) {
		this.firefoxProfile = firefoxProfile;
	}

	public String getFirefoxProfile() {
		return firefoxProfile;
	}

	public void setSeleniumServerPort(String seleniumServerPort) {
		this.seleniumServerPort = seleniumServerPort;
	}

	public String getSeleniumServerPort() {
		return seleniumServerPort;
	}

}
