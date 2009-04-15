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
