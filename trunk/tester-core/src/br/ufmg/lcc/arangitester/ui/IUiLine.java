package br.ufmg.lcc.arangitester.ui;

/**
 * Represent a single line in a table
 * @author Lucas Gonçalves
 *
 */
public interface IUiLine extends IUiComponent, IUiComposite{
	void click();
	
	/**
	 * Called when the line is added to a table. It sets the new index to the children of this line 
	 * @param index on table
	 */
	void setIndex(int index);
	int getIndex();
}
