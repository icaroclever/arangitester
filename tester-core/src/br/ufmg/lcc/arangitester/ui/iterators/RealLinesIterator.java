package br.ufmg.lcc.arangitester.ui.iterators;

import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.ui.IUiLine;
import br.ufmg.lcc.arangitester.ui.IUiTable;

/**
 * Iterate only over a table. Open all existent line and iterate over then.
 * 
 * @author Lucas Gonçalves
 *
 */
public class RealLinesIterator extends ComponentsIteratorBase {
	private IUiTable<? extends IUiLine> root;

	public RealLinesIterator(IUiTable<? extends IUiLine> root) {
		this.root = root;
	}

	private void createList(IUiTable<? extends IUiLine> table) {
		boolean existAtLeastOne = false;
		int i=0;
		do{
			existAtLeastOne = false;
			IUiLine line = table.getLine(i++);
			for ( IUiComponent compOfLine: line ){
				if ( compOfLine.exist() ){
					existAtLeastOne = true;
					break;
				}
			}
			if ( existAtLeastOne ){
				getComponentsList().add(line);
			}
		}while(existAtLeastOne);
	}

	@Override
	protected void createList() {
		createList(root);
	}
}
