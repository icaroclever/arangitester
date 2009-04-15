package br.ufmg.lcc.arangitester.ui.iterators;

import br.ufmg.lcc.arangitester.ui.IUiComponent;


/**
 * Iterator for UiComponents. It iterate recursively to all child components
 * 
 * @author Lucas Gonçalves
 * 
 */

public class FullComponentsIterator extends ComponentsIteratorBase {
	private IUiComponent root;

	public FullComponentsIterator(IUiComponent root) {
		this.root = root;
	}

	private void createList(IUiComponent comp) {
		getComponentsList().add(comp);
		for (IUiComponent child : comp) {
			createList(child);
		}
	}

	@Override
	protected void createList() {
		createList(root);
		getComponentsList().remove(0);
	}
}
