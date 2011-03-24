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
package br.ufmg.lcc.arangitester.ui;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.ui.actions.UiClickable;

/**
 * Represent a Div.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class UiDiv extends UiClickable {

	public String getTextInside() {
		return getSel().getText(getComponentLocator());
	}

	/**
	 * Verify text inside the div. But don't try again if it isn't there.
	 * Return true if any expectedMessage is present
	 */
	public boolean isTextInsidePresentWithOutWait(String[] expectedMessage) {
		for (String expected : expectedMessage) {
			if (isTextInsidePresentWithOutWait(expected)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Verify text inside the div. But don't try again if it isn't there.
	 */
	public boolean isTextInsidePresentWithOutWait(String expectedMessage) {
		expectedMessage = StringUtils.deleteWhitespace(expectedMessage);
		String texto = getSel().getText(getComponentLocator());
		texto = StringUtils.deleteWhitespace(texto);
		if (texto.contains(expectedMessage)) {
			return true;
		}
		return false;
	}

}
