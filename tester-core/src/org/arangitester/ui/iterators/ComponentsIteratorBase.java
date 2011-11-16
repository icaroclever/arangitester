/*
 * Copyright 2000 Universidade Federal de Minas Gerais.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arangitester.ui.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.arangitester.ui.IUiComponent;


/**
 * Base of all iterators. It iterate over a List named componentsList.
 * On iterator creation its necessary to fill the componentsList. For it override the
 * abstract method createList on subclasses.
 * 
 * @author Lucas Gonçalves
 * 
 */
public abstract class ComponentsIteratorBase implements Iterator<IUiComponent> {
	private List<IUiComponent>	componentsList;
	private int					position	= -1;

	protected abstract void createList();

	public List<IUiComponent> getComponentsList() {
		if (componentsList == null) {
			componentsList = new ArrayList<IUiComponent>();
			createList();
		}
		return componentsList;
	}

	public boolean hasNext() {
		if (getComponentsList().size() > position + 1) {
			return true;
		}
		return false;
	}

	public IUiComponent next() {
		position++;
		return getComponentsList().get(position);
	}

	public void remove() {}

}
