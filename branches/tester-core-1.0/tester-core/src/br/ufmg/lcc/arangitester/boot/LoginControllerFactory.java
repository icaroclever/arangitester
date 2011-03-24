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
package br.ufmg.lcc.arangitester.boot;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.exceptions.EnvException;

/**
 * Instaciate a loginController from config.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class LoginControllerFactory {
	private static Logger	LOG	= Logger.getLogger(LoginControllerFactory.class);

	public static ILoginController getLoginController() {
		String loginController = ConfigFactory.getConfig().getLoginController();
		if (StringUtils.isBlank(loginController)) {
			return null;
		}
		try {
			Class<?> clazz = Class.forName(loginController);
			LOG.debug("Carregando Login Controller: " + clazz.getName());
			return (ILoginController) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new EnvException("Classe de controle de login não existe: " + loginController);
		} catch (InstantiationException e) {
			throw new EnvException("Error ao carregar a classe de controle de login: " + loginController);
		} catch (IllegalAccessException e) {
			throw new EnvException("Error ao carregar a classe de controle de login: " + loginController);
		}
	}
}
