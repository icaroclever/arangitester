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

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ioc.ICreate;
import br.ufmg.lcc.arangitester.ui.actions.UiClickable;

public class UiCheckBox extends UiClickable implements ICreate{
	
	public String getStatus() {
		return getSel().getValue(getComponentLocator());
	}
	
	@Logger("Preenchendo #{componentDesc}: check")
	public void check(){
		setPreviewslyActionValue(true);
		if(!getSel().getValue(getComponentLocator()).equals("on")) {
			getSel().click(getComponentLocator());
		}
		
		if(!isChecked()) throw new ArangiTesterException("Erro em check. O checkbox não foi marcado com sucesso");
	}
	
	@Logger("Preenchendo #{componentDesc}: uncheck")
	public void uncheck(){
		setPreviewslyActionValue(false);
		if(getSel().getValue(getComponentLocator()).equals("on")) {
			getSel().click(getComponentLocator());
		}
		
		if(isChecked()) throw new ArangiTesterException("Erro em check. O checkbox não foi desmarcado com sucesso");
	}
	
	public boolean isChecked(){
		return getSel().getValue(getComponentLocator()).equals("on");
	}
	
	@Logger("Verificando #{componentDesc}: #0")
	public void verifyValue(Boolean state){
		if ( state != null ){
			boolean actual = getSel().getValue(getComponentLocator()).equals("on"); 
			if ( state != actual ){
				throw new WrongValueException("O valor esperado é: " + state + 
						" mas o presente é: '" + actual + "'");
			}
		}
	}
	
	@Logger("Verificando #{componentDesc}: $0")
	public void verifyCheckBox(String expectedStatus) {
		waitElement(getComponentLocator());
		String realValue = getSel().getValue(getComponentLocator());
		if ( !realValue.equals(expectedStatus.trim())) {
			throw new WrongValueException("Valor esperado: " + expectedStatus + ". Valor atual: '" + realValue + "'");
		}
	}
	
	@Override
	public void verifyPreviewslyAction() {
		Boolean expectedValue = (Boolean)getPreviewslyActionValue();
		if (expectedValue != null)
			verifyValue(expectedValue);
	}
	
	/**
	 * Metodo create. 
	 * @see ICreate
	 * Neste caso, seta o submit padrao para false
	 */
	@Override
	public void create(){
		super.create();
		getRequestConfig().setSubmit(false);
	}
}
