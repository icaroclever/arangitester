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
package br.ufmg.lcc.arangitester.pages;

import br.ufmg.lcc.arangitester.annotations.Page;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.ui.UiArangiPage;
import br.ufmg.lcc.arangitester.ui.UiButton;
import br.ufmg.lcc.arangitester.ui.UiInputText;

/**
 * Generic login page.
 * 
 * @author Lucas Gonçalves
 *
 */
@Page(url="/")
public class ArangiLoginPage extends UiArangiPage {

	@Ui(desc="Autenticar", locator="id=btnOk")
	private UiButton btnOk;
	
	@Ui(desc="User name", locator="name=j_username")
	private UiInputText username;

	@Ui(desc="Password", locator="name=j_password")
	private UiInputText password;
	
	
	public ArangiLoginPage(){
		super();
	}
	
	public UiInputText getUsername() {
		return username;
	}

	public void setUsername(UiInputText username) {
		this.username = username;
	}

	public UiInputText getPassword() {
		return password;
	}

	public void setPassword(UiInputText password) {
		this.password = password;
	}

	public UiButton getBtnOk() {
		return btnOk;
	}

	public void setBtnOk(UiButton btnOk) {
		this.btnOk = btnOk;
	}

}
