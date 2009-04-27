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

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.LccException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.ui.iterators.RealLinesIterator;

public class UiTable<T extends IUiLine> extends UiComponent implements IUiTable<T>{
	private Class<T> type;
	
	private List<T> lines = new ArrayList<T>();

	@Override
	@SuppressWarnings("unchecked")
	public Iterator<IUiComponent> iterator() {
		Iterator<?> iterator = lines.iterator();
		return (Iterator<IUiComponent>)iterator;
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<IUiLine> createRealLinesIterator(){
		Iterator<?> iterator = new RealLinesIterator(this);
		return (Iterator<IUiLine>) iterator ;
	}
	
	public T addLine(boolean begin){
		T newLine = UiComponentFactory.getInstance(getType());
		newLine.setParent(this);
		
		if ( begin ){
			for ( IUiLine line : lines ){
				line.setIndex( line.getIndex() + 1);
			}
			newLine.setIndex(0);
			lines.add(0, newLine);
		} else {
			newLine.setIndex(lines.size());
			lines.add((T) newLine);
		}
		return (T)newLine;
	}

	@Override
	public void removeLine(int number) {
		for (int i=number; i< lines.size(); i++){
			((T)getLines().get(number)).setIndex(i + 1);
		}
		getLines().remove(number);
	}
	
	@Logger("Verifing number of lines in a table #{componentDesc}: #0")
	public void verifyBodyLines(int number){
		int realLines = getRealLinesNumber();
		if ( realLines != number ){
			throw new WrongValueException("Expected " + number + " lines on table " + getComponentDesc() 
					+ " but there are " + realLines + " lines");
		}
	}
	
	/**
	 * Returns the number of the first line which text contains the string 
	 * passed as param. 
	 * @param text
	 * @return Number of the first line that contains the string passed as 
	 * param, or -1 if the text was not faound
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	//TODO Refazer essa função, não bem específicamente.
	public T getLineFromContent(String text) {
		Iterator<IUiLine> iterator = createRealLinesIterator();
		IUiLine line;
		String xpath;
		while(iterator.hasNext()) {
			line = iterator.next();
			xpath = "xpath=//"+super.locator.getHtmlNameSpace()+"table[@id='"+getComponentId()+"']/"+super.locator.getHtmlNameSpace()+"tbody/"+super.locator.getHtmlNameSpace()+"tr["+(line.getIndex()+1)+"]";
			
			if(getSel().getText(xpath).contains(text))
				return (T)line;
		}
		throw new LccException("Nenhuma linha com o texto: " +text);
	}
	
	public boolean existLineFromContent(String text)
	{
		String line = getSel().getText("xpath=//"+super.locator.getHtmlNameSpace()+"/tr"+super.locator.getHtmlNameSpace()+"/td");
		return line.contains(text);
	}
	
	/**
	 * Trasfome Component id or name in a xpath locator.
	 * @return Xpath Locator for table.
	 */
	public String getTableLocatorInPath() {
        if (StringUtils.isNotBlank(super.getComponentId())) {
            return String.format("xpath=//%stable[@id='%s']", super.locator.getHtmlNameSpace(), super.getComponentId());
        } else if (StringUtils.isNotBlank(super.getComponentName())) {
            return String.format("xpath=//%stable[@name='%s']", super.locator.getHtmlNameSpace(), super.getComponentName());
        } else {
            if (!super.getComponentLocator().startsWith("xpath=")) {
                throw new TesterException("Locator for table must be a xpath locator. If possible use id or name attribute on annotation.");
            }
            return super.getComponentLocator();
        }
    }
	
	/**
	 * 
	 * @param lineNumber
	 * @return
	 */
	public String getLineContent(int lineNumber){
	    String xpath = String.format("%s/%stbody/%str[%s]", this.getTableLocatorInPath(), super.locator.getHtmlNameSpace(), super.locator.getHtmlNameSpace(), lineNumber);
		return getSel().getText(xpath);
	}
	
	@Logger("Clicking at line [#0]")
	public void clickAtLine(int lineNumber){
	    String xpath = String.format("%s/%stbody/%str[%s]", this.getTableLocatorInPath(), super.locator.getHtmlNameSpace(), super.locator.getHtmlNameSpace(), lineNumber);
		getSel().click(xpath);
	}
	
	@Override
	@Logger("Verifying text on table[#0][#1] : #3")
	public void verifyColumnText(int line, int col, Section section, String text) {
		//default:table[@id='registerForm:globalParameterTable']/default:thead/default:tr[1]/default:th[3]
		String sectionStr = section.toString().toLowerCase();
		String xpath = "xpath=//"+super.locator.getHtmlNameSpace()+"table[@id='" + getComponentId() +"']/"+super.locator.getHtmlNameSpace()+"" + sectionStr + "/"+super.locator.getHtmlNameSpace()+"tr[" +
				line + "]/"+super.locator.getHtmlNameSpace()+"td[" + col + "]";
		String actualText = getSel().getText(xpath);
		String actualWithoutSpaces = StringUtils.deleteWhitespace(actualText);
		String expectedTextWithoutSpaces = StringUtils.deleteWhitespace(text);
		if ( !actualWithoutSpaces.equals(expectedTextWithoutSpaces) ){
			throw new WrongValueException("Expected value is: " + text + 
					" but actually is: '" + actualText + "'");
		}
		
	}
	
	public void verifyAllPreviewslyActions(){
		for ( T line: getLines() ){
			line.verifyAllPreviewslyActions();
		}
	}
	
	@Override
	public void verifyPreviewslyAction() {
		verifyAllPreviewslyActions();
	}
	
	public int getRealLinesNumber(){
		try{
			waitElement(getComponentId());
		}catch (ElementNotExistException e) {
			throw new ElementNotExistException("Erro. Tabela não encontrada. Desc: "+ getComponentDesc()+". Id:  "+ getComponentId());
		}
		
		String xpath = "//"+super.locator.getHtmlNameSpace()+"table[@id='" + getComponentId() +"']/"+super.locator.getHtmlNameSpace()+"tbody/"+super.locator.getHtmlNameSpace()+"tr";
		Number eval = getSel().getXpathCount(xpath);
		return eval.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public void verifyAllEnabled(boolean enable, Class... components){
		for ( T line: getLines() ){
			line.verifyAllEnabled(enable, components);
		}
	}
	
	@Override
	public void verifyAllEnable(boolean enable) {
		getLine(2);
		for ( T line: getLines() ){
			line.verifyAllEnable(enable);
		}
	}
	
	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}
	
	public T getLine(int i){
		while ( lines.size() <= i){
			addLine(false);
		}
		return lines.get(i);
	}

	public List<T> getLines() {
		return lines;
	}
}