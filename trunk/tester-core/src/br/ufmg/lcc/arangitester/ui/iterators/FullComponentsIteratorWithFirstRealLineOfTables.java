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
