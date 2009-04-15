package br.ufmg.lcc.arangitester.ui;

/**
 * Implements a tab. 
 */
public class UiTab extends UiClickable {
	@Override
	public void verifyPreviewslyAction() {
		this.click();		
	}
	
	@Override
	public void validade(UiButton saveButton, UiDiv divMessage) {
		this.clickWithOutLogger();
	}
}
