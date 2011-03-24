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
package org.arangitester.util;

import org.arangitester.ui.UiButton;
import org.arangitester.ui.UiCheckBox;
import org.arangitester.ui.UiComponent;
import org.arangitester.ui.UiInputText;
import org.arangitester.ui.UiRadio;
import org.arangitester.ui.UiSelect;
import org.arangitester.ui.UiTab;

/**
 * Help use el in component actions like fill and verify.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class ComponentElUtil {

	/**
	 * Given a variable name of a component and a value, the variable component will be filled.
	 * 
	 * @param name
	 *            UiComponent variable name.
	 * @param value
	 *            Value to be filled.
	 * @param component
	 *            UiComponent with children. param name is a atribute of this component.
	 */
	public static void fill(String name, String value, UiComponent component) {
		ElHelper el = new ElHelper();
		el.addVariable("this", component);
		Object target = el.resolveElExpression(String.format("${this.%s}", name));
		if (target instanceof UiInputText) {
			((UiInputText) target).type(value);
		} else if (target instanceof UiSelect) {
			((UiSelect) target).select(value);
		} else if (target instanceof UiCheckBox) {
			if (value.toLowerCase().equals("sim")) {
				((UiCheckBox) target).check();
			} else {
				((UiCheckBox) target).uncheck();
			}
		} else if (target instanceof UiRadio) {
			((UiRadio) target).select(value);
		} else if (target instanceof UiButton) {
			((UiButton) target).click();
		} else if (target instanceof UiTab) {
			if (value.toLowerCase().equals("click")) {
				((UiTab) target).click();
			}
		}
	}
}
