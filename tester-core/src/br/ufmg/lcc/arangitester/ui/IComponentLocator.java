package br.ufmg.lcc.arangitester.ui;

public interface IComponentLocator {
	String getHtmlNameSpace();
	String getComponentId(String id, IUiComponent component);
	String getComponentName(String name, IUiComponent component);
	String getComponentLocator(String locator, IUiComponent component);
}
