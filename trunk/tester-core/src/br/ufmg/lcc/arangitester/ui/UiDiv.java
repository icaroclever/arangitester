package br.ufmg.lcc.arangitester.ui;

import org.apache.commons.lang.StringUtils;

/**
 * Represent a Div.
 * @author Lucas Gonçalves
 *
 */
public class UiDiv extends UiClickable{
	
	public String getTextInside(){
		return getSel().getText(getComponentLocator());
	}

	/**
	 * Verify text inside the div. But don't try again if it isn't there.
	 * Return true if any expectedMessage is present 
	 */
	public boolean isTextInsidePresentWithOutWait(String[] expectedMessage){
		for (String expected: expectedMessage ){
			if (isTextInsidePresentWithOutWait(expected)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Verify text inside the div. But don't try again if it isn't there. 
	 */
	public boolean isTextInsidePresentWithOutWait(String expectedMessage){
		expectedMessage = StringUtils.deleteWhitespace(expectedMessage);
		String texto = getSel().getText(getComponentLocator());
		texto = StringUtils.deleteWhitespace(texto);
		if ( texto.contains(expectedMessage) ){
			return true;
		} 
		return false;
	}

}
