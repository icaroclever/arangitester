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
