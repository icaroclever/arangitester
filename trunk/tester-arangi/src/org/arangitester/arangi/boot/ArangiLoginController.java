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
package org.arangitester.arangi.boot;

import org.arangitester.Context;
import org.arangitester.annotations.Field;
import org.arangitester.arangi.pages.ArangiLoginPage;
import org.arangitester.boot.BaseLoginController;
import org.arangitester.exceptions.ElementNotExistException;
import org.arangitester.exceptions.FatalException;
import org.arangitester.exceptions.InvokeException;
import org.arangitester.ioc.UiComponentFactory;
import org.arangitester.ui.UiPage;


import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Reponsable to control loggin between test cases.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class ArangiLoginController extends BaseLoginController {

	protected void login(String user, String password, Field[] fields) throws FatalException {
		Selenium sel = Context.getInstance().getSeleniumController().getSeleniumClient();
		try{
			ArangiLoginPage login = UiComponentFactory.getInstance(ArangiLoginPage.class);
			login.invoke();
			login.getUsername().type(user);
			login.getPassword().type(password);
			login.getBtnOk().click();
		}catch (InvokeException e) {
			throw new FatalException("Falha no carregamento da página de login");
		}catch (ElementNotExistException e) {
			throw new FatalException("Erro fatal: " + e.getMessage());
		}
		
		// Verifica se o login foi bem sucedido
		try{
			sel.waitForPageToLoad(UiPage.DEFAULT_PAGE_WAIT_TIME);
		}catch (SeleniumException e) {
			throw new FatalException("Falha no login: " + e.getMessage());
		}
		if(!sel.getLocation().contains("Home.faces")) throw new FatalException("Falha no login.");
		
		
	}
}
