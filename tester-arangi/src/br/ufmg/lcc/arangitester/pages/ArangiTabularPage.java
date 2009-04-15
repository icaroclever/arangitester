package br.ufmg.lcc.arangitester.pages;

import java.util.Iterator;
import java.util.List;

import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.ui.GenericLine;
import br.ufmg.lcc.arangitester.ui.IUiLine;
import br.ufmg.lcc.arangitester.ui.IUiTable;
import br.ufmg.lcc.arangitester.ui.UiArangiPage;
import br.ufmg.lcc.arangitester.ui.UiTable;

/**
 * Generic Tabular Page. It wraps a LccUiTable.
 * @author Lucas Gonçalves
 *
 * @param <T> Type of line.
 */
public class ArangiTabularPage<T extends GenericLine> extends UiArangiPage implements IUiTable<T>{

	private UiTable<T> table;
	
	private Class<T> lineType;
	
	/**
	 * Line type.
	 * @param lineType type of line. This must be passed, for instanciated new lines.
	 */
	public ArangiTabularPage(Class<T> lineType) {
		super();
		this.lineType = lineType;
	}


	@SuppressWarnings("unchecked")
	public UiTable<T> getTable() {
		if ( table == null ){
			table = UiComponentFactory.getInstance(UiTable.class);
			table.setType(lineType);
		}
		return table;
	}
	
	public void verifyAllPreviewslyActions(){
		getTable().verifyAllPreviewslyActions();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void verifyAllEnabled(boolean enabled,  Class... components) {
		getTable().verifyAllEnabled(enabled, components);
	}
	
	//
	// DELEGATE METHOD FOR TABLE
	//
	@Override
	public void removeLine(int number) {
		getTable().removeLine(number);
		
	}

	@Override
	public void verifyBodyLines(int number) {
		getTable().verifyBodyLines(number);
	}

	@Override
	public List<T> getLines() {
		return getTable().getLines();
	}

	@Override
	public T addLine(boolean begin) {
		return getTable().addLine(begin);
	}

	@Override
	public T getLine(int i) {
		return getTable().getLine(i);
	}

	@Override
	public Class<T> getType() {
		return getTable().getType();
	}

	@Override
	public void setType(Class<T> type) {
		getTable().setType(type);
	}

	@Override
	public void verifyColumnText(int line, int col,	Section section, String text) {
		getTable().verifyColumnText(line, col, section, text);
	}

	@Override
	public int getRealLinesNumber() {
		return getTable().getRealLinesNumber();
	}

	@Override
	public Iterator<IUiLine> createRealLinesIterator() {
		return getTable().createRealLinesIterator();
	}
}
