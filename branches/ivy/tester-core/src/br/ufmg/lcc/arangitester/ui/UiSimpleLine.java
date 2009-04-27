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

import java.lang.reflect.Field;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.ui.iterators.ComponentsIterator;
import br.ufmg.lcc.arangitester.util.ElHelper;
import br.ufmg.lcc.arangitester.util.Refletions;


/**
 * @author Lucas Gonçalves
 */
public class UiSimpleLine extends UiComponent implements IUiLine {
    protected int index;

    @Override
    public Iterator<IUiComponent> iterator() {
        return new ComponentsIterator(this);
    }

    @Override
    public String getComponentId() {
        return super.getComponentId();
    }

    /**
     * Change all ids, locators and desc of this line children.
     */
    public void setIndex(int index) {
        this.index = index;
        for (Field field : Refletions.getFields(this.getClass(), Ui.class)) {
            Ui uiConfig = field.getAnnotation(Ui.class);
            try {
                IUiComponent ui = (IUiComponent) Refletions.getFieldValue(field, this);
                String locator = uiConfig.locator();
                String id = uiConfig.id();

                ElHelper el = new ElHelper();
                el.addVariable("tableXpath", getParent().getComponentLocator());
                el.addVariable("tableId", getParent().getComponentId());
                el.addVariable("tableName", getParent().getComponentName());
                el.addVariable("index", index);

                if (StringUtils.isNotBlank(id)) {
                    ui.setComponentId((String) el.resolveElExpression(id));
                }

                if (StringUtils.isNotBlank(locator)) {
                    ui.setComponentLocator((String) el.resolveElExpression(locator));
                }

                ui.setComponentDesc((String) el.resolveElExpression(uiConfig.desc()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void verifyAllEnabled(boolean enable, Class... components) {
        for (IUiComponent component : Refletions.getAllUiComponents(this, components)) {
            component.verifyIsEnable(enable);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void verifyAllEnable(boolean enable) {
        Class[] components = new Class[] {UiInputText.class, UiCheckBox.class, UiSelect.class};
        for (IUiComponent component : Refletions.getAllUiComponents(this, components)) {
            if (component.exist())
                component.verifyIsEnable(enable);
        }
    }

    @Override
    public void verifyAllPreviewslyActions() {
        for (IUiComponent component : Refletions.getAllUiComponents(this.getClass(), this)) {
            component.verifyPreviewslyAction();
        }
    }

    @Logger("Clicking at line [#index]")
    public void click() {
        String xpath = "xpath=//" + super.locator.getHtmlNameSpace() + "table[@id='" + getComponentId() + "']/" + super.locator.getHtmlNameSpace() + "tbody/" + super.locator.getHtmlNameSpace() + "tr[" + index + "]";
        getSel().click(xpath);
    }

    public int getIndex() {
        return index;
    }

}
