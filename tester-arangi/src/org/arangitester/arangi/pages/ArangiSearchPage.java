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

import org.apache.log4j.Logger;
import org.arangitester.arangi.ui.GenericLine;
import org.arangitester.boot.Reactor;
import org.arangitester.exceptions.WrongValueException;
import org.arangitester.ioc.UiComponentFactory;
import org.arangitester.ui.UiTable;


public class ArangiSearchPage extends ArangiPage{
	
	private UiTable<GenericLine> result;
	private Class<? extends GenericLine> line = GenericLine.class;
	
	@SuppressWarnings("unchecked")
	public ArangiSearchPage(String tableName, Class<? extends GenericLine> line) {
		super();
		result = (UiTable<GenericLine>)UiComponentFactory.getInstance(UiTable.class);
		result.setComponentId(tableName);
		result.setComponentDesc("Result");
		if(line != null)
			this.line = line;
	}
	
	public ArangiSearchPage(String tableName)
	{
		this(tableName,null);
	}

	public void verifyResult(int number){
		Logger LOG = Logger.getLogger(Reactor.class);
		try {
			getBtnSearch().click();
			getResult().verifyBodyLines(number);
		} catch( WrongValueException e ) {
			LOG.error(e.getMessage());
		}
	}
	
	class Teste extends GenericLine
	{
		
	}
	
	public UiTable<? extends GenericLine> getResult()
	{
		if ( result != null )
			result.setType(line);
			
		return (UiTable<? extends GenericLine>) result;
	}
	
	
	/**
	 * Returns the line of the table result in the SearchPage corresponding to
	 * the id passed as param. 
	 * @param id The id of the element in the table result. Related with the database.
	 * @return The line of the table result corresponding to the id. 0 Based!
	 */
	public int getLinePositionFromRegistryId(int id){
		// TODO Não faz sentido o código. Ele simplesmente verifica se existe o ID e manda deletar sempre o mesma linha, idependente do ID
		return id;
				
		/*String xpath = "//"+this.locator.getHtmlNameSpace()+"a[contains(@href,'event=view&id="+id+"')]";// and substring-after(@href,'event=view&id="+id+"')='']/ancestor::"+this.locator.getHtmlNameSpace()+"tr[1]+/preceding-sibling::"+this.locator.getHtmlNameSpace()+"*";
		System.out.println(xpath);
		int linePosition = UiPage.getSel().getXpathCount(xpath).intValue(); //0 Based!
		System.out.println(linePosition);
		return linePosition;*/
	}
	
	
	
}
