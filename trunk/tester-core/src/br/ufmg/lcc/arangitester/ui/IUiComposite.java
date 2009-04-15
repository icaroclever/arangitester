package br.ufmg.lcc.arangitester.ui;

/**
 * Component that has other components inside it.
 * @author Lucas Gonçalves
 *
 */
public interface IUiComposite extends IUiComponent{
	
	/**
	 * Recursive call verifyEnabled for all components on this container
	 * @param components SubClasses of component will be verified. Ex. LccUiInputText, all 
	 * LccUiInputText including subclasses will be verified.
	 * @param enable true enable, false disable
	 */
	@SuppressWarnings("unchecked")
	void verifyAllEnabled(boolean enable, Class... components);

	/**
	 * This is a helper method. It recursive verify if components exist and verify if its enable.
	 * On tables, start with index 0 and go on tring adding +1 on index until it doesn't exist.
	 * @param enable true enable, false disable
	 */
	public void verifyAllEnable(boolean enable);
	
	/**
	 * Recursive call verifyPreviewslyAction on components inside this container
	 */
	public void verifyAllPreviewslyActions();
	
}