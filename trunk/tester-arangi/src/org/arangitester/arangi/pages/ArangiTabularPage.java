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
package org.arangitester.arangi.pages;

import java.util.Iterator;
import java.util.List;

import org.arangitester.arangi.ui.GenericLine;
import org.arangitester.ioc.UiComponentFactory;
import org.arangitester.ui.IUiLine;
import org.arangitester.ui.IUiTable;
import org.arangitester.ui.UiTable;


/**
 * Generic Tabular Page. It wraps a LccUiTable.
 * @author Lucas Gonçalves
 * @author Ícaro Clever Braga
 *
 * @param <T> Type of line.
 */
public class ArangiTabularPage<T extends GenericLine> extends ArangiPage implements IUiTable<T>{

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
	public void verifyAllEnabled(boolean enabled,  @SuppressWarnings("rawtypes") Class... components) {
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
	public List<? extends T> getLines() {
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
	public Class<? extends T> getType() {
		return getTable().getType();
	}

	@Override
	public void setType(Class<? extends T> type) {
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
