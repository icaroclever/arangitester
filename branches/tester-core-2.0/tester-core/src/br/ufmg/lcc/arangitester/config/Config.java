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

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author Lucas Gonçalves
 * 
 */
@XStreamAlias("tester-config")
public class Config {

	@XStreamAlias("default")
	@XStreamAsAttribute
	private String			defaultEnv;

	@XStreamAlias("login-controller")
	private String			loginController;

	@XStreamAlias("locator-class")
	private String			locatorClass;

	@XStreamAlias("default-login-username")
	private String			defaultLoginUsername;

	@XStreamAlias("default-login-password")
	private String			defaultLoginPassword;

	@XStreamImplicit
	private List<ConfigEnv>	environments;

	@XStreamAlias("tomcat-users")
	private String			tomcatUsers;

	public List<ConfigEnv> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<ConfigEnv> environments) {
		this.environments = environments;
	}

	public String getDefaultEnv() {
		return defaultEnv;
	}

	public void setDefaultEnv(String defaultEnv) {
		this.defaultEnv = defaultEnv;
	}

	public String getTomcatUsers() {
		return tomcatUsers;
	}

	public void setTomcatUsers(String tomcatUsers) {
		this.tomcatUsers = tomcatUsers;
	}

	public String getLoginController() {
		return loginController;
	}

	public void setLoginController(String loginController) {
		this.loginController = loginController;
	}

	public String getLocatorClass() {
		return locatorClass;
	}

	public void setLocatorClass(String locatorClass) {
		this.locatorClass = locatorClass;
	}

	public String getDefaultLoginUsername() {
		return defaultLoginUsername;
	}

	public void setDefaultLoginUsername(String defaultLoginUsername) {
		this.defaultLoginUsername = defaultLoginUsername;
	}

	public String getDefaultLoginPassword() {
		return defaultLoginPassword;
	}

	public void setDefaultLoginPassword(String defaultLoginPassword) {
		this.defaultLoginPassword = defaultLoginPassword;
	}

}
