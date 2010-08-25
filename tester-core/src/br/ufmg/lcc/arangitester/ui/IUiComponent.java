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

import java.lang.annotation.Annotation;

import br.ufmg.lcc.arangitester.ui.actions.IUiClickable;

/**
 * Components that represent Tags on HTML. Like inputtext, div, select...
 * It use Composite Pattern, permiting create components bigger then one tag, like tables
 * and a whole page.
 * 
 * Composites must implements {@link IUiComposite}
 * 
 * @author Lucas Gonçalves
 * 
 */
public interface IUiComponent extends Iterable<IUiComponent> {
	void setConfigs(Annotation[] configs);

	<T extends Annotation> T getConfig(Class<T> annotation);

	String getComponentDesc();

	void setComponentDesc(String name);

	String getComponentLocator();

	void setComponentLocator(String locator);

	String getComponentId();

	void setComponentId(String id);

	String getComponentName();

	void setComponentName(String name);

	void setParent(IUiComponent parent);

	IUiComponent getParent();

	String getComponentIndex();

	void setComponentIndex(String id);

	/**
	 * Verify value in the component based on preview action executed.
	 * Components that implement this method must verify if previews action is not null
	 */
	void verifyPreviewslyAction();

	Object getPreviewslyActionValue();

	void clearPreviewslyAction();

	/**
	 * Element is enabled when it is ENABLED and NOT READ-ONLY.
	 * and is not enable when it is DISABLED OR READ-ONLY.
	 * 
	 */
	void verifyIsEnable(boolean enable);

	boolean exist();

	void click();

	void doubleClick();

	String getText();

	/**
	 * Force validation on field.
	 */
	void validate(IUiClickable clickableElement, IUiComponent component);
}