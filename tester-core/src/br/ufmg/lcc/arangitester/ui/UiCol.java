package br.ufmg.lcc.arangitester.ui;

import br.ufmg.lcc.arangitester.annotations.Logger;

public class UiCol extends UiComponent {
	
	@Logger("Verifing text: #0")
	public void verifyText(String text){
		getSel().getText(getComponentLocator());
	}
}
