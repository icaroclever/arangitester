package br.ufmg.lcc.arangitester.php;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.ui.IComponentLocator;
import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.util.LccStringUtils;

/**
 * @author Lucas Gonçalves
 * 
 */
public class PhpLocator implements IComponentLocator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getComponentId(java.lang.String, br.ufmg.lcc.arangitester.ui.LccIUiComponent)
	 */
	@Override
	public String getComponentId(String id, IUiComponent component) {
		return LccStringUtils.interpolate(id, component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getComponentLocator(java.lang.String, br.ufmg.lcc.arangitester.ui.LccIUiComponent)
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
	 * 
	 * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getComponentName(java.lang.String, br.ufmg.lcc.arangitester.ui.LccIUiComponent)
	 */
	@Override
	public String getComponentName(String name, IUiComponent component) {
		return LccStringUtils.interpolate(name, component);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufmg.lcc.arangitester.ui.LccIComponentLocator#getHtmlNameSpace()
	 */
	@Override
	public String getHtmlNameSpace() {
		return "";
	}

}
