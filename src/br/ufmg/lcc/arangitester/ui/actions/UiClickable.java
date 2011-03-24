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
package br.ufmg.lcc.arangitester.ui.actions;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.RequestConfigImp;
import br.ufmg.lcc.arangitester.ioc.ICreate;
import br.ufmg.lcc.arangitester.ui.UiComponent;
import br.ufmg.lcc.arangitester.ui.actions.IRequest.IRequestCommand;

/**
 * Permit a common action click().
 * 
 * @author Lucas Gonçalves
 * 
 */
public class UiClickable extends UiComponent implements ICreate, IUiClickable {
	private Request	requestDelegate;

	public void clickWithOutLogger() {
		waitElement(getComponentLocator());
		super.mouseOver();
		IRequestCommand action = new IRequestCommand() {

			@Override
			public void execute() {
				getSel().click(getComponentLocator());
			}

		};
		requestDelegate.execute(action, getSel());
	}

	/**
	 * Click on button
	 */
	@Logger("Clicando: #{componentDesc}")
	public void click() {
		clickWithOutLogger();
	}

	public RequestConfigImp getRequestConfig() {
		return requestDelegate.getRequestConfig();
	}

	@Override
	public void create() {
		requestDelegate = new Request(getConfig(RequestConfig.class));
	}

	@Override
	public String getComponentTag() {
		// TODO Auto-generated method stub
		return null;
	}

}
