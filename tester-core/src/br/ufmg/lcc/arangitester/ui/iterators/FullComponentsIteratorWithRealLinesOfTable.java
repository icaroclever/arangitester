package br.ufmg.lcc.arangitester.ui.iterators;

import java.util.Iterator;

import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.ui.IUiLine;
import br.ufmg.lcc.arangitester.ui.IUiTable;


/**
 * Iterate over components and in composites components. IF is a table, verify witch lines are present o browser, 
 * and iterate over this lines to. If on table model there isn't any line, this iterator has to create line and verify if it
 * exist on Browser.
 * 
 * @author Lucas Gonçalves
 *
 */
public class FullComponentsIteratorWithRealLinesOfTable extends ComponentsIteratorBase {
	private IUiComponent root;

	public FullComponentsIteratorWithRealLinesOfTable(IUiComponent root) {
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