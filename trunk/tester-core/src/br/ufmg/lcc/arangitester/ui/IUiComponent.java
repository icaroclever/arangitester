package br.ufmg.lcc.arangitester.ui;

import java.lang.annotation.Annotation;

/**
 * Components that represent Tags on HTML. Like inputtext, div, select...
 * It use Composite Pattern, permiting create components bigger then one tag, like tables
 * and a whole page.
 * 
 * Composites must implements {@link IUiComposite}
 * @author Lucas Gonçalves
 *
 */
public interface IUiComponent extends Iterable<IUiComponent>{
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
	
	/**
	 * Verify value in the component based on preview action executed.
	 * Components that implement this method must verify if previews action is not null
	 */
	void verifyPreviewslyAction();
	Object getPreviewslyActionValue();
	void clearPreviewslyAction();
	
	void verifyIsEnable(boolean enable);
	boolean exist();
	
	String getText();
	/**
	 * Force validation on field.
	 */
	void validade(UiButton button, UiDiv divMessage);
}