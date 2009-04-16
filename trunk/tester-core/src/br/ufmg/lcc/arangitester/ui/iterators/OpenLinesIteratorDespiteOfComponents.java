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
package br.ufmg.lcc.arangitester.ui.iterators;

import br.ufmg.lcc.arangitester.ui.IUiLine;
import br.ufmg.lcc.arangitester.ui.IUiTable;

/**
 * Iterate only over a table. Open all existent line and iterate over then, 
 * independent if the lines have components.
 * 
 * @author Lucas Gonçalves
 *
 */
public class OpenLinesIteratorDespiteOfComponents extends ComponentsIteratorBase {
	private IUiTable<? extends IUiLine> root;

	public OpenLinesIteratorDespiteOfComponents(IUiTable<? extends IUiLine> root) {
		this.root = root;
	}

	private void createList(IUiTable<? extends IUiLine> table) {
		int i=0;
		do{
			IUiLine line = table.getLine(i++);
			if (line.exist()){
				getComponentsList().add(line);
			} else{
				break;
			}
		}while(true);
	}

	@Override
	protected void createList() {
		createList(root);
	}
}
