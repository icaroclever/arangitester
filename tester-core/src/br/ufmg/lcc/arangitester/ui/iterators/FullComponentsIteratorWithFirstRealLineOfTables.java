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

import java.util.Iterator;

import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.ui.IUiLine;
import br.ufmg.lcc.arangitester.ui.IUiTable;

/**
 * It is like {@link FullComponentsIteratorWithRealLinesOfTable} but iterate only on FIRST line.
 * Used on validation because we assume that if the first line validations is working, then others
 * too, on that case saving a lot of time of test execution.
 * 
 * @author Lucas Gonçalves
 *
 */
public class FullComponentsIteratorWithFirstRealLineOfTables extends ComponentsIteratorBase {

		private IUiComponent root;

		public FullComponentsIteratorWithFirstRealLineOfTables(IUiComponent root) {
			this.root = root;
		}

		@SuppressWarnings("unchecked")
		private void createList(IUiComponent comp) {
			getComponentsList().add(comp);
			if ( comp instanceof IUiTable){
				IUiTable<? extends IUiLine> table = (IUiTable<? extends IUiLine>)comp;
				Iterator<IUiLine> lineIterator = table.createRealLinesIterator();
				while (lineIterator.hasNext()) {
					IUiLine line = (IUiLine) lineIterator.next();
					createList(line);
				}
			}else{
				for (IUiComponent child: comp) {
					createList(child);
				}
			}
		}

		@Override
		protected void createList() {
			createList(root);
			getComponentsList().remove(0);
		}
	
}
