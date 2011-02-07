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
package br.ufmg.lcc.arangitester.ui;

import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author Lucas Gonçalves
 * 
 */
public interface IUiTable<T extends IUiLine> extends IUiComposite {
	T addLine(boolean begin);

	/**
	 * Remove a line. 0 Based.
	 */
	void removeLine(int number);

	Class<? extends T> getType();

	void setType(Class<? extends T> type);

	void verifyAllPreviewslyActions();

	Iterator<IUiLine> createRealLinesIterator();

	/**
	 * Get lines on the table on browser! It's not the same as getLines().size().
	 * 
	 * @return Number of lines in body part of table.
	 */
	public int getRealLinesNumber();

	/**
	 * Verify number of lines in a table.
	 * 
	 * @param number
	 *            of line must be present
	 */
	public void verifyBodyLines(int number);

	/**
	 * 0 Based!
	 * 
	 * @param i
	 *            number of line
	 * @return line with fields.
	 */
	T getLine(int i);

	List<? extends T> getLines();

	/**
	 * Verify text in a cell. Spaces between words will be discarted.
	 * 
	 * @param line
	 *            number started with 1.
	 * @param col
	 *            number started with 1.
	 * @param section
	 *            section on table.
	 * @param text
	 *            must be on cell.
	 */
	void verifyColumnText(int line, int col, Section section, String text);

	/**
	 * Represent basic sections on a table
	 */
	enum Section {
		THEAD, TBODY, TFOOT
	}
}
