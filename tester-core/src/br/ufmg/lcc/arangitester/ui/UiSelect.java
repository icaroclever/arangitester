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

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.RequestConfigImp;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ioc.ICreate;
import br.ufmg.lcc.arangitester.ui.actions.Request;
import br.ufmg.lcc.arangitester.ui.actions.IRequest.IRequestCommand;

import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

/**
 * Represent a simple select tag on HTML. This component can do request to server.
 * 
 * @see RequestConfig
 * @author Lucas Gonçalves
 * 
 */
public class UiSelect extends UiComponent implements ICreate {
	private Request	requestDelegate;

	public void create() {
		requestDelegate = new Request(super.getConfig(RequestConfig.class));
		requestDelegate.getRequestConfig().setSubmit(false);
	}

	/**
	 * Select a option in select tag.
	 * 
	 * @param label
	 *            caption of option
	 */
	@Logger("Selecionando opção em #{componentDesc}: '#0'")
	public void select(final String label) {
		selectWithOutLog(label);
	}

	public void selectWithOutLog(final String label) {
		waitElement(getComponentLocator());
		waitOption(getComponentLocator(), label);

		if (!getSel().isSomethingSelected(getComponentLocator()) || !(label.equals(getSel().getSelectedLabel(getComponentLocator())))) {

			IRequestCommand action = new IRequestCommand() {

				public void execute() {
					getSel().select(getComponentLocator(), "label=" + label + "");
				}

			};

			requestDelegate.execute(action, getSel());

			setPreviewslyActionValue(label);
		}
	}

	@Logger("Verificando '#{componentDesc}': '#0'")
	public void verifySelect(String label) {
		waitElement(getComponentLocator());
		try {
			waitOption(getComponentLocator(), label);
		} catch (ElementNotExistException e) {
			throw new WrongValueException("A opção " + label + "não existe no select.");
		}
		String selectedLabel = getSel().getSelectedLabel(getComponentLocator());
		if (!selectedLabel.equals(label)) {
			throw new WrongValueException("Expected option: " + label +
					" but actually is: '" + selectedLabel + "'");
		}
	}

	public String getSelectedLabel() {
		waitElement(getComponentLocator());

		try {
			new Wait() {
				@Override
				public boolean until() {
					return getSel().getSelectedLabel(getComponentLocator()) != null;
				}
			}.wait(getComponentLocator(), DEFAULT_ELEMENT_WAIT_TIME);
		} catch (WaitTimedOutException e) {
			return null;
		}
		return getSel().getSelectedLabel(getComponentLocator());
	}

	private void waitOption(final String locator, final String label) {
		try {
			new Wait() {
				@Override
				public boolean until() {
					return ArrayUtils.contains(getSel().getSelectOptions(locator), label);
				}
			}.wait(locator, DEFAULT_ELEMENT_WAIT_TIME);
		} catch (WaitTimedOutException e) {
			throw new ElementNotExistException("Opção '" + label + "' não encontrada.\n" +
					"Opções existentes: " + Arrays.toString(getSel().getSelectOptions(locator)));
		}
	}

	@Override
	public void verifyPreviewslyAction() {
		String expectedText = (String) getPreviewslyActionValue();
		if (expectedText != null)
			verifySelect(expectedText);
	}

	public RequestConfigImp getRequestConfig() {
		return requestDelegate.getRequestConfig();
	}

	@Override
	public String getComponentTag() {
		return "select";
	}
}
