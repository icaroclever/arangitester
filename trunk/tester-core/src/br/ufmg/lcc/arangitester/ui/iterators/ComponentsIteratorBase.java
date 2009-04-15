package br.ufmg.lcc.arangitester.ui.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ufmg.lcc.arangitester.ui.IUiComponent;

/**
 * Base of all iterators. It iterate over a List named componentsList.
 * On iterator creation its necessary to fill the componentsList. For it override the
 * abstract method createList on subclasses.
 * 
 * @author Lucas Gonçalves
 *
 */
public abstract class ComponentsIteratorBase implements Iterator<IUiComponent>{
	private List<IUiComponent> componentsList;
	private int position = -1;
	
	protected abstract void createList();
	
	public List<IUiComponent> getComponentsList(){
		if ( componentsList == null ){
			componentsList = new ArrayList<IUiComponent>();
			createList();
		}
		return componentsList;
	}
	
	@Override
	public boolean hasNext() {
		if ( getComponentsList().size() > position + 1){
			return true;
		}
		return false;
	}

	@Override
	public IUiComponent next() {
		position++;
		return getComponentsList().get(position);
	}

	@Override
	public void remove() {
	}

}
