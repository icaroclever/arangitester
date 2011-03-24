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
package org.arangitester.arangi.ui;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.arangitester.annotations.RequestConfig;
import org.arangitester.annotations.Ui;
import org.arangitester.ui.IUiComponent;
import org.arangitester.ui.UiCheckBox;
import org.arangitester.ui.UiSimpleLine;
import org.arangitester.ui.actions.IRequest;
import org.arangitester.ui.actions.UiClickable;
import org.arangitester.util.ArangiTesterStringUtils;
import org.arangitester.util.Refletions;


public class GenericLine extends UiSimpleLine{

	@Ui(desc = "Delete #{index}", id = "checkDelete")
	private UiCheckBox checkDelete;

	@Ui(desc = "Modify #{index}", id = "modifyImg")
	private UiClickable modify;

	@Ui(desc = "Save #{index}", id = "saveImg")
	private UiClickable save;

	@RequestConfig(window=IRequest.Window.CLOSE)
	@Ui(desc = "Select #{index}", id = "selectImg")
	private UiClickable select;
	
	@RequestConfig(confirmation=IRequest.Confirmation.OK)
	@Ui(desc = "Cancelar  #{index}", id = "cancelImg")
	private UiClickable cancel;
	
	/**
     * Change all ids, locators and desc of this line children.
     */
    public void setIndex(int index) {
        this.index = index;
        for(Field field: Refletions.getFields(this.getClass(), Ui.class)){
            Ui uiConfig = field.getAnnotation(Ui.class);
            try {
                IUiComponent ui = (IUiComponent)Refletions.getFieldValue(field, this);
                
                if(ui == null) continue;
                String locator = uiConfig.locator();
                String id = uiConfig.id();
                String name = uiConfig.name();
                
                if (StringUtils.isNotBlank(id)) {
                    id = getParent().getComponentId() + ":#{index}:" + uiConfig.id();
                    ui.setComponentId(ArangiTesterStringUtils.interpolate(id, this));
                }
                else if(StringUtils.isNotBlank(name)) {
                    id = getParent().getComponentId() + ":#{index}:" + uiConfig.name();
                    ui.setComponentId(ArangiTesterStringUtils.interpolate(name, this));
                } 
                
                if (StringUtils.isNotBlank(locator)) {
                    ui.setComponentLocator(ArangiTesterStringUtils.interpolate(locator, this));
                }
                
                ui.setComponentDesc(ArangiTesterStringUtils.interpolate(uiConfig.desc(), this));
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
	public UiCheckBox getCheckDelete() {
		return checkDelete;
	}

	public void setCheckDelete(UiCheckBox checkDelete) {
		this.checkDelete = checkDelete;
	}

	public UiClickable getModify() {
		return modify;
	}

	public void setModify(UiClickable modify) {
		this.modify = modify;
	}

	public UiClickable getSave() {
		return save;
	}

	public void setSave(UiClickable save) {
		this.save = save;
	}

	public UiClickable getSelect() {
		return select;
	}

	public void setSelect(UiClickable select) {
		this.select = select;
	}

	public UiClickable getCancel() {
		return cancel;
	}

	public void setCancel(UiClickable cancel) {
		this.cancel = cancel;
	}
}
