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

import org.arangitester.ui.IUiComponent;
import org.arangitester.util.Refletions;

/**
 * Iterate over a direct children of composited component.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class ComponentsIterator extends ComponentsIteratorBase {
	private IUiComponent	root;

	public ComponentsIterator(IUiComponent root) {
		this.root = root;
	}

	@Override
	protected void createList() {
		getComponentsList().addAll(Refletions.getAllUiComponents(root));
	}

}
