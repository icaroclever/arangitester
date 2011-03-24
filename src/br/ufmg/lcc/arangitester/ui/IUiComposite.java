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

import br.ufmg.lcc.arangitester.annotations.VerifyAjax;

/**
 * Component that has other components inside it.
 * 
 * @author Lucas Gonçalves
 * 
 */
public interface IUiComposite extends IUiComponent {

	/**
	 * Recursive call verifyEnabled for all components on this container
	 * 
	 * @param components
	 *            SubClasses of component will be verified. Ex. LccUiInputText, all
	 *            LccUiInputText including subclasses will be verified.
	 * @param enable
	 *            true enable, false disable
	 */
	@VerifyAjax
	void verifyAllEnabled(boolean enable, @SuppressWarnings("rawtypes") Class... components);

	/**
	 * This is a helper method. It recursive verify if components exist and verify if its enable.
	 * On tables, start with index 0 and go on tring adding +1 on index until it doesn't exist.
	 * 
	 * @param enable
	 *            true enable, false disable
	 */
	@VerifyAjax
	public void verifyAllEnable(boolean enable);

	/**
	 * Recursive call verifyPreviewslyAction on components inside this container
	 */
	@VerifyAjax
	public void verifyAllPreviewslyActions();

}