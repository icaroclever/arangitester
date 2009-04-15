package br.ufmg.lcc.arangitester;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.ui.IComponentLocator;
import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.util.LccStringUtils;

public class ArangiComponentLocator implements IComponentLocator {

	@Override
	public String getComponentId(String componentId, IUiComponent component) {
		if ( StringUtils.isNotBlank(componentId)){
			if ( componentId.startsWith("registerForm") ){
				return LccStringUtils.interpolate(componentId, component);
			}
			return "registerForm:" + LccStringUtils.interpolate(componentId, component);	
		}
		return null;
	}

	@Override
	public String getComponentLocator(String componentLocator, IUiComponent component) {
		if ( StringUtils.isNotBlank(componentLocator) ){
			return LccStringUtils.interpolate(componentLocator, component);
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
				return LccStringUtils.interpolate(componentName, component);
			}
			return "registerForm:" + LccStringUtils.interpolate(componentName, component);	
		}
		return null;
	}

	@Override
	public String getHtmlNameSpace() {
		return "";
	}

}
