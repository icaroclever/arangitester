package br.ufmg.lcc.arangitester.ui.iterators;

import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.util.Refletions;

/**
 * Iterate over a direct children of composited component.
 * @author Lucas Gonçalves
 *
 */
public class ComponentsIterator extends ComponentsIteratorBase {
	private IUiComponent root;

	public ComponentsIterator(IUiComponent root) {
		this.root = root;
	}

	@Override
	protected void createList() {
		 getComponentsList().addAll(Refletions.getAllUiComponents(root));
	}

}
