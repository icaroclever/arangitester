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
package org.arangitester.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.arangitester.boot.LoginControllerFactory;
import org.arangitester.config.ConfigFactory;
import org.arangitester.exceptions.EnvException;


/**
 * Create instance of {@link IComponentLocator} from tester-config.xml configuration. If no one is configured
 * then {@link DefaultLocator} will be use.
 * 
 * @author Lucas Gonçalves
 */
public class LocatorFactory {

	private static Logger	LOG	= Logger.getLogger(LoginControllerFactory.class);

	public static IComponentLocator getLocator() {
		String loginController = ConfigFactory.getConfig().getLocatorClass();

		try {
			Class<?> clazz = null;

			if (StringUtils.isEmpty(loginController)) {
				clazz = DefaultLocator.class;
			} else {
				clazz = Class.forName(loginController);
			}
			LOG.debug("Carregando Login Controller: " + loginController);
			return (IComponentLocator) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new EnvException("Classe Locator não existe: " + loginController);
		} catch (Exception e) {
			throw new EnvException("Error ao carregar a classe Locator: " + loginController);
		}

	}
}
