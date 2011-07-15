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
package br.ufmg.lcc.arangitester.arangi.boot;

import br.ufmg.lcc.arangitester.annotations.Field;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiLoginPage;
import br.ufmg.lcc.arangitester.boot.BaseLoginController;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.FatalException;
import br.ufmg.lcc.arangitester.exceptions.InvokeException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Reponsable to control loggin between test cases.
 * 
 * @author Lucas Gonçalves
 * @author Ícaro Clever
 * 
 */
public class ArangiLoginController extends BaseLoginController {
		
	protected void login(String user, String password, Field[] fields) throws FatalException {
		ArangiLoginPage login = UiComponentFactory.getInstance(ArangiLoginPage.class);
		try{
			login.invoke();
			login.getUsername().type(user);
			login.getPassword().type(password);
			login.getBtnOk().click();
		}catch (InvokeException e) {
			throw new FatalException("Falha no carregamento da página de login");
		}catch (ElementNotExistException e) {
			throw new FatalException("Erro fatal: " + e.getMessage());
		}catch (SeleniumException e) {
			throw new FatalException("Falha no login: " + e.getMessage());
		}
		
		if(login.getUsername().exist()) throw new FatalException("Falha no login: usuário ou senha inválidos");	
	}
}
