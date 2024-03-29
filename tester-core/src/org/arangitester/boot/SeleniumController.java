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
package org.arangitester.boot;

import java.io.File;

import org.arangitester.Context;
import org.arangitester.config.ConfigEnv;
import org.arangitester.exceptions.EnvException;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;


import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Control server and client components of Selenium
 * 
 * @author Lucas Gonçalves
 * 
 */
public class SeleniumController {

	private SeleniumServer			seleniumServer;
	private Selenium				seleniumClient;
	private ConfigEnv				config;
	private static final Integer	DEFAULT_SELENIUM_SERVER_PORT	= 6889;

	public SeleniumController(ConfigEnv config) {
		this.config = config;
	}

	/**
	 * Destroys client clicking in close button
	 */
	public void destroyClient() {
		if (seleniumClient != null) {
			try {
				seleniumClient.close();
			} catch (Exception e) {
				Context.getInstance().getResult().addError("O browser não foi inicializado, portanto não " +
						"há necessidade de clicar no botão fechar.");
			}
			seleniumClient.stop();
			seleniumClient = null;
		}
	}

	/**
	 * Destroys client ending the test session, killing the browser
	 */
	public void killClient() {
		if (seleniumClient != null) {
			seleniumClient.stop();
			seleniumClient = null;
		}
	}

	public Selenium getSeleniumClient() {
		if (seleniumClient == null) {

			// log.debug("Starting selenium client");
			// log.debug(" browser: " +config.getBrowser() + " url:" + getUrl());
			seleniumClient = new DefaultSelenium("localhost", getServer().getPort(), config.getBrowser(), getUrl());
			seleniumClient.start();
			try {
				seleniumClient.getBodyText();
			} catch (Exception e) {
				if (e.getMessage().contains("sessionId should not be null"))
					throw new EnvException("Não foi possível inicializar o selenium." +
							" Verifique as configurações do mesmo. Browser, servidor, porta, etc.", e);
			}
		}
		return seleniumClient;
	}

	private SeleniumServer getServer() {

		if (seleniumServer == null) {
			try {
				int selServerPort = (config.getSeleniumServerPort() != null && !config.getSeleniumServerPort().equals("")) ?
						new Integer(config.getSeleniumServerPort()).intValue() : DEFAULT_SELENIUM_SERVER_PORT;

				// log.debug("Starting selenium server");
				RemoteControlConfiguration remoteControlConfiguration = new RemoteControlConfiguration();
				remoteControlConfiguration.setSingleWindow(true);
				remoteControlConfiguration.setDontTouchLogging(true);
				remoteControlConfiguration.setBrowserSideLogEnabled(false);
				remoteControlConfiguration.setReuseBrowserSessions(false);
				remoteControlConfiguration.setPort(selServerPort);
				if (config.getFirefoxProfile() != null && !config.getFirefoxProfile().equals(""))
					remoteControlConfiguration.setFirefoxProfileTemplate(new File(config.getFirefoxProfile()));
				seleniumServer = new SeleniumServer(remoteControlConfiguration);
				seleniumServer.start();
			} catch (Exception e) {
				throw new EnvException("It's not possible to start selenium server!", e);
			}
		}
		return seleniumServer;
	}

	private String getUrl() {
		return "http://" + config.getHost() + ":" + config.getPort();
	}

	/**
	 * Stop Selenium Server end clients
	 */
	public void stop() {
		if (seleniumClient != null)
			destroyClient();
		if (seleniumServer != null)
			seleniumServer.stop();
	}
}
