package br.ufmg.lcc.arangitester.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.LccException;
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
	public T getLineFromContent(String text) {
		Iterator<IUiLine> iterator = createRealLinesIterator();
		IUiLine line;
		while(iterator.hasNext()) {
			if((line = iterator.next()).getText().contains(text))
				return (T)line;
		}
		throw new LccException("Nenhuma linha com o texto: " +text);

	}
	
	public String getLineContent(int lineNumber){
		String xpath = "xpath=//"+super.locator.getHtmlNameSpace()+"table[@id='" + getComponentId() +"']/"+super.locator.getHtmlNameSpace()+"tbody/"+super.locator.getHtmlNameSpace()+"tr[" +	lineNumber + "]";
		return getSel().getText(xpath);
	}
	
	@Logger("Clicking at line [#0]")
	public void clickAtLine(int lineNumber){
		String xpath = "xpath=//"+super.locator.getHtmlNameSpace()+"table[@id='" + getComponentId() +"']/"+super.locator.getHtmlNameSpace()+"tbody/"+super.locator.getHtmlNameSpace()+"tr[" + lineNumber + "]";
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
		
		String xpath = "//"+super.locator.getHtmlNameSpace()+"table[@id='" + getComponentId() +"']/tbody/tr";
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