package br.ufmg.lcc.arangitester.pages;

import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.boot.Reactor;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.ui.GenericLine;
import br.ufmg.lcc.arangitester.ui.UiArangiPage;
import br.ufmg.lcc.arangitester.ui.UiPage;
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
		
		String xpath = "//"+this.locator.getHtmlNameSpace()+"a[contains(@href,'event=view&id="+id+"')]";// and substring-after(@href,'event=view&id="+id+"')='']/ancestor::"+this.locator.getHtmlNameSpace()+"tr[1]+/preceding-sibling::"+this.locator.getHtmlNameSpace()+"*";
		int linePosition = UiPage.getSel().getXpathCount(xpath).intValue(); //0 Based!
		return linePosition;
	}
	
	
	
}
