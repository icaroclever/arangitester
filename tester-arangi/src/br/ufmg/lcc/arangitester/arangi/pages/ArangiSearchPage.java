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
package br.ufmg.lcc.arangitester.arangi.pages;

import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.arangi.ui.UiArangiPage;
import br.ufmg.lcc.arangitester.boot.Reactor;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.ui.GenericLine;
import br.ufmg.lcc.arangitester.ui.UiTable;

public class ArangiSearchPage extends UiArangiPage{

	private String tableName = null;
	
	@SuppressWarnings("unchecked")
	private UiTable result;
	
	public ArangiSearchPage(String tableName) {
		super();
		this.tableName = tableName;
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
	
	@SuppressWarnings("unchecked")
	public UiTable<? extends GenericLine> getResult(){
		if ( result == null ){
			result = (UiTable<? extends GenericLine>)UiComponentFactory.getInstance(UiTable.class);
			result.setType(GenericLine.class);
			result.setComponentId(tableName);
			result.setComponentDesc("Result");
		}
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
