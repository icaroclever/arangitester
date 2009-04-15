package br.ufmg.lcc.arangitester.ui;

import java.lang.reflect.Field;
import java.util.Iterator;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.ui.iterators.ComponentsIterator;
import br.ufmg.lcc.arangitester.util.Refletions;
import br.ufmg.lcc.arangitester.util.LccStringUtils;

public class UiSimpleLine extends UiComponent implements IUiLine{
	private int index;

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
		for(Field field: Refletions.getFields(this.getClass(), Ui.class)){
			Ui uiConfig = field.getAnnotation(Ui.class);
			try {
				IUiComponent ui = (IUiComponent)Refletions.getFieldValue(field, this);
				String locator = uiConfig.locator();
				String id = uiConfig.id();
				
				id = getParent().getComponentId() + ":#{index}:" + uiConfig.id(); 
				ui.setComponentLocator(LccStringUtils.interpolate(locator, this));	
				ui.setComponentDesc(LccStringUtils.interpolate(uiConfig.desc(), this));
				ui.setComponentId(LccStringUtils.interpolate(id, this));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void verifyAllEnabled(boolean enable, Class... components) {
		for (IUiComponent component: Refletions.getAllUiComponents(this, components)){
			component.verifyIsEnable(enable);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void verifyAllEnable(boolean enable){
		Class[] components = new Class[]{UiInputText.class, UiCheckBox.class, UiSelect.class};
		for (IUiComponent component: Refletions.getAllUiComponents(this, components)){
			if ( component.exist() )
				component.verifyIsEnable(enable);
		}
	}
	
	@Override
	public void verifyAllPreviewslyActions() {
		for (IUiComponent component: Refletions.getAllUiComponents(this.getClass(), this)){
			component.verifyPreviewslyAction();
		}
	}
	
	@Logger("Clicking at line [#index]")
	public void click(){
		String xpath = "xpath=//" + super.locator.getHtmlNameSpace() + "table[@id='" + getComponentId() +"']/" + super.locator.getHtmlNameSpace() + "tbody/" + super.locator.getHtmlNameSpace() + "tr[" +	index + "]";
		getSel().click(xpath);
	}

	public int getIndex() {
		return index;
	}

}
