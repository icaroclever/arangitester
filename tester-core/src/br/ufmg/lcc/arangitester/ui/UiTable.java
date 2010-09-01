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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.selenium.Wait;
import br.ufmg.lcc.arangitester.annotations.Line;
import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.VerifyAjax;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.ui.iterators.RealLinesIterator;

public class UiTable<T extends IUiLine> extends UiComponent implements IUiTable<T> {

	private Class<T>	type;

	private List<T>		lines	= new ArrayList<T>();

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<IUiComponent> iterator() {
		Iterator<?> iterator = lines.iterator();
		return (Iterator<IUiComponent>) iterator;
	}

	@SuppressWarnings("unchecked")
	public Iterator<IUiLine> createRealLinesIterator() {
		Iterator<?> iterator = new RealLinesIterator(this);
		return (Iterator<IUiLine>) iterator;
	}

	public T addLine(boolean begin) {
		T newLine = UiComponentFactory.getInstance(getType());
		newLine.setParent(this);

		if (begin) {
			for (IUiLine line : lines) {
				line.setIndex(line.getIndex() + 1);
			}
			newLine.setIndex(0);
			lines.add(0, newLine);
		} else {
			newLine.setIndex(lines.size());
			lines.add((T) newLine);
		}
		return (T) newLine;
	}

	@Override
	public void removeLine(int number) {
		for (int i = number; i < lines.size(); i++) {
			((T) getLines().get(number)).setIndex(i + 1);
		}
		getLines().remove(number);
	}

	@Logger("Verifing number of lines in a table #{componentDesc}: #0")
	public void verifyBodyLines(final int number) {
		new Wait() {
			@Override
			public boolean until() {
				return (getRealLinesNumber() == number);
			}
		}.wait("Error", DEFAULT_ELEMENT_WAIT_TIME);

		int realLines = getRealLinesNumber();
		if (realLines != number) {
			throw new WrongValueException("Expected " + number + " lines on table " + getComponentDesc()
					+ " but there are " + realLines + " lines");
		}
	}

	/**
	 * Gets a xPath locator using the table's id or name .
	 * 
	 * @return Xpath Locator for table.
	 */
	public String getTableLocatorInXPath() {
		if (StringUtils.isNotBlank(super.getComponentId())) {
			return String.format("xpath=//%stable[@id='%s']", super.locator.getHtmlNameSpace(), super.getComponentId());
		} else if (StringUtils.isNotBlank(super.getComponentName())) {
			return String.format("xpath=//%stable[@name='%s']", super.locator.getHtmlNameSpace(), super.getComponentName());
		} else {
			if (!super.getComponentLocator().startsWith("xpath=")) {
				throw new ArangiTesterException("Locator for table must be a xpath locator. If possible use id or name attribute on annotation.");
			}
			return super.getComponentLocator();
		}
	}

	/**
	 * Returns the number of the first line which text contains the string
	 * passed as param.
	 * 
	 * @param text
	 * @return Number of the first line that contains the string passed as
	 *         param, or -1 if the text was not faound
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	// TODO Refazer essa função, não bem específicamente.
	@VerifyAjax
	public T getLineFromContent(String text) {
		Iterator<IUiLine> iterator = createRealLinesIterator();
		IUiLine line;
		String xpath;
		while (iterator.hasNext()) {
			line = iterator.next();
			xpath = "xpath=//" + super.locator.getHtmlNameSpace() + "table[@id='" + getComponentId() + "']/" + super.locator.getHtmlNameSpace() + "tbody/"
					+ super.locator.getHtmlNameSpace() + "tr[" + (line.getIndex() + 1) + "]";

			if (getSel().getText(xpath).contains(text))
				return (T) line;
		}
		throw new ArangiTesterException("Nenhuma linha com o texto: " + text);
	}

	public boolean existLineFromContent(String text) {
		String line = getSel().getText("xpath=//" + super.locator.getHtmlNameSpace() + "/tr" + super.locator.getHtmlNameSpace() + "/td");
		return line.contains(text);
	}

	/**
	 * @deprecated use getLine(lineNumber).getContext()
	 *             Retrive all texts inside line.
	 * @param lineNumber
	 *            to get text.
	 * 
	 * @see IUiLine
	 */
	public String getLineContent(int lineNumber) {
		return getLine(lineNumber).getContent();
	}

	/**
	 * @deprecated use getLine(lineNumber).click();
	 */
	public void clickAtLine(int lineNumber) {
		getLine(lineNumber).click();
	}

	@Override
	@Logger("Verifying text on table[#0][#1] : #3")
	public void verifyColumnText(int line, int col, Section section, String text) {
		// default:table[@id='registerForm:globalParameterTable']/default:thead/default:tr[1]/default:th[3]
		String sectionStr = section.toString().toLowerCase();
		String xpath = "xpath=//" + super.locator.getHtmlNameSpace() + "table[@id='" + getComponentId() + "']/" + super.locator.getHtmlNameSpace() + "" + sectionStr + "/"
				+ super.locator.getHtmlNameSpace() + "tr[" +
				line + "]/" + super.locator.getHtmlNameSpace() + "td[" + col + "]";
		String actualText = getSel().getText(xpath);
		String actualWithoutSpaces = StringUtils.deleteWhitespace(actualText);
		String expectedTextWithoutSpaces = StringUtils.deleteWhitespace(text);
		if (!actualWithoutSpaces.equals(expectedTextWithoutSpaces)) {
			throw new WrongValueException("Expected value is: " + text +
					" but actually is: '" + actualText + "'");
		}

	}

	public void verifyAllPreviewslyActions() {
		for (T line : getLines()) {
			line.verifyAllPreviewslyActions();
		}
	}

	@Override
	public void verifyPreviewslyAction() {
		verifyAllPreviewslyActions();
	}

	public int getRealLinesNumber() {
		try {
			waitElement(getComponentLocator());
		} catch (ElementNotExistException e) {
			throw new ElementNotExistException("Table has not found.\nDescription: " + getComponentDesc() + ".\nId:  " + getComponentId());
		}

		String xpath = String.format("/%1$s/%2$stbody/%2$str", this.getXPathLocator(), super.locator.getHtmlNameSpace()); // Eg.: xpath = "//table[@id='ID'/tbody/tr]";

		Number eval = getSel().getXpathCount(xpath);

		Line lineAnnotation = getType().getAnnotation(Line.class);
		if (lineAnnotation != null) {
			eval = Math.max(eval.intValue() - lineAnnotation.beginIndex(), 0);
			if (lineAnnotation.footer()) {
				eval = Math.max(eval.intValue() - 1, 0);
			}
		}
		return eval.intValue();
	}

	@SuppressWarnings("unchecked")
	public void verifyAllEnabled(boolean enable, Class... components) {
		for (T line : getLines()) {
			line.verifyAllEnabled(enable, components);
		}
	}

	@Override
	public void verifyAllEnable(boolean enable) {
		getLine(2);
		for (T line : getLines()) {
			line.verifyAllEnable(enable);
		}
	}

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public T getLine(int i) {
		while (lines.size() <= i) {
			addLine(false);
		}
		return lines.get(i);
	}

	public List<T> getLines() {
		return lines;
	}

	@Override
	public String getComponentTag() {
		return "table";
	}
}