package br.ufmg.lcc.arangitester.ui;

import org.apache.commons.lang.StringUtils;
import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;

public class UiRadio extends UiComponent{

	@Logger("Clicking on radio #{componentDesc} with value '#0'")
	public void select(String value){
		String xpath = getXPath(value);
		waitElement(xpath);
		getSel().click(xpath);
		setPreviewslyActionValue(value);
	}
	
	@Logger("Verifing radio #{componentDesc} with value '#0' is selected")
	public void verifySelect(String value){
		String xpath = getXPath(value);
		waitElement(xpath);
		boolean checked = getSel().isChecked(xpath);
		if ( !checked ){
			throw new WrongValueException("Expected radio " + getComponentDesc() + " with value " +
					value + " be check. But it isn't");
		}
	}

	@Logger("Verifing radio #{componentDesc} with value '#0' is NOT selected")
	public void verifyNotSelect(String value){
		String xpath = getXPath(value);
		waitElement(xpath);
		boolean checked = getSel().isChecked(xpath);
		if ( checked ){
			throw new WrongValueException("Expected radio " + getComponentDesc() + " with value " +
					value + " be check. But it isn't");
		}
	}
	
	private String getXPath(String value){
		String xpath = "xpath=//" + super.locator.getHtmlNameSpace() + "input[";
		if (StringUtils.isNotBlank(getComponentId()) ){
			xpath += "@id='" + getComponentId() + "'"; 
		} else{
			xpath += "@name='" + getComponentName() + "'";
		}
		xpath += " and @value='" + value +"']";
		return xpath;
	}
	
	@Override
	public void verifyPreviewslyAction() {
		verifySelect((String)getPreviewslyActionValue());
	}
	
}
