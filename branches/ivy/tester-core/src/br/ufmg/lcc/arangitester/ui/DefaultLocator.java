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

import br.ufmg.lcc.arangitester.util.LccStringUtils;

public class DefaultLocator implements IComponentLocator {

    /*
     * (non-Javadoc)
     * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getComponentId(java.lang.String,
     *      br.ufmg.lcc.arangitester.ui.LccIUiComponent)
     */
    @Override
    public String getComponentId(String id, IUiComponent component) {
        return LccStringUtils.interpolate(id, component);
    }

    /*
     * (non-Javadoc)
     * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getComponentLocator(java.lang.String,
     *      br.ufmg.lcc.arangitester.ui.LccIUiComponent)
     */
    @Override
    public String getComponentLocator(String componentLocator, IUiComponent component) {
        if (StringUtils.isNotBlank(componentLocator)) {
            return LccStringUtils.interpolate(componentLocator, component);
        } else if (StringUtils.isNotBlank(component.getComponentId())) {
            return "id=" + component.getComponentId();
        } else if (StringUtils.isNotBlank(component.getComponentName())) {
            return "name=" + component.getComponentName();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getComponentName(java.lang.String,
     *      br.ufmg.lcc.arangitester.ui.LccIUiComponent)
     */
    @Override
    public String getComponentName(String name, IUiComponent component) {
        return LccStringUtils.interpolate(name, component);
    }

    /*
     * (non-Javadoc)
     * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getHtmlNameSpace()
     */
    @Override
    public String getHtmlNameSpace() {
        return "";
    }

}