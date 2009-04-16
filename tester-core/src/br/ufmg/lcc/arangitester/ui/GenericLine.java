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

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.util.LccStringUtils;
import br.ufmg.lcc.arangitester.util.Refletions;

public class GenericLine extends UiSimpleLine{

	@Ui(desc = "Delete #{index}", id = "checkDelete")
	private UiCheckBox checkDelete;

	@Ui(desc = "View #{index}", id = "viewImg")
	private UiImage view;

	@Ui(desc = "Modify #{index}", id = "modifyImg")
	private UiImage modify;

	@Ui(desc = "Save #{index}", id = "saveImg")
	private UiImage save;

	@RequestConfig(window=IRequest.Window.CLOSE)
	@Ui(desc = "Select #{index}", id = "selectImg")
	private UiImage select;
	
	@RequestConfig(confirmation=IRequest.Confirmation.OK)
	@Ui(desc = "Cancelar  #{index}", id = "cancelImg")
	public UiImage cancel;
	
	/**
     * Change all ids, locators and desc of this line children.
     */
    public void setIndex(int index) {
        this.index = index;
        for(Field field: Refletions.getFields(this.getClass(), Ui.class)){
            Ui uiConfig = field.getAnnotation(Ui.class);
            try {
                IUiComponent ui = (IUiComponent)Refletions.getFieldValue(field, this);
                String locator = uiConfig.locator();
                String id = uiConfig.id();
                
                if (StringUtils.isNotBlank(id)) {
                    id = getParent().getComponentId() + ":#{index}:" + uiConfig.id();
                    ui.setComponentId(LccStringUtils.interpolate(id, this));
                }
                
                if (StringUtils.isNotBlank(locator)) {
                    ui.setComponentLocator(LccStringUtils.interpolate(locator, this));
                }
                
                ui.setComponentDesc(LccStringUtils.interpolate(uiConfig.desc(), this));
                
                
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

	public UiImage getView() {
		return view;
	}

	public void setView(UiImage view) {
		this.view = view;
	}

	public UiImage getModify() {
		return modify;
	}

	public void setModify(UiImage modify) {
		this.modify = modify;
	}

	public UiImage getSave() {
		return save;
	}

	public void setSave(UiImage save) {
		this.save = save;
	}

	public UiImage getSelect() {
		return select;
	}

	public void setSelect(UiImage select) {
		this.select = select;
	}

	public UiImage getCancel() {
		return cancel;
	}

	public void setCancel(UiImage cancel) {
		this.cancel = cancel;
	}
}
