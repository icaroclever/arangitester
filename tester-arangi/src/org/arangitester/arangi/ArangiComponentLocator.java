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
package org.arangitester.arangi;

import org.apache.commons.lang.StringUtils;
import org.arangitester.ui.IComponentLocator;
import org.arangitester.ui.IUiComponent;
import org.arangitester.util.ArangiTesterStringUtils;


public class ArangiComponentLocator implements IComponentLocator {

	@Override
	public String getComponentId(String componentId, IUiComponent component) {
		if ( StringUtils.isNotBlank(componentId)){
			if ( componentId.startsWith("registerForm") ){
				return ArangiTesterStringUtils.interpolate(componentId, component);
			}
			return "registerForm:" + ArangiTesterStringUtils.interpolate(componentId, component);	
		}
		return null;
	}

	@Override
	public String getComponentLocator(String componentLocator, IUiComponent component) {
		if ( StringUtils.isNotBlank(componentLocator) ){
			return ArangiTesterStringUtils.interpolate(componentLocator, component);
		}else if ( StringUtils.isNotBlank(component.getComponentId())){
			return "id=" + component.getComponentId();
		} else if ( StringUtils.isNotBlank(component.getComponentName())){
			return "name=" + component.getComponentName();
		}
		return null;
	}

	@Override
	public String getComponentName(String componentName, IUiComponent component) {
		if ( StringUtils.isNotBlank(componentName)){
			if ( componentName.startsWith("registerForm") ){
				return ArangiTesterStringUtils.interpolate(componentName, component);
			}
			return "registerForm:" + ArangiTesterStringUtils.interpolate(componentName, component);	
		}
		return null;
	}

	@Override
	public String getHtmlNameSpace() {
		return "";
	}

}
