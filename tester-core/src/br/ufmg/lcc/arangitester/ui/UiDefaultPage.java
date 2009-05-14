package br.ufmg.lcc.arangitester.ui;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Page;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;

/**
 * This class extends UiPage and uses annotation @Page 
 * @author Ícaro Clever da Fonseca Braga
 */
public class UiDefaultPage extends UiPage {

	/**
	 * 	Invokes a page, using values of @Page annotation
	 * */
	public void invoke()
	{
		invoke(getPageUrl(),getConfig().title());
	}
	
	/**
	 * Return Page annotated on subclass of UiPage
	 * @return null if not exist
	 */
	public Page getConfig(){
		return super.getClass().getSuperclass().getAnnotation(Page.class);
	}
	
	/**
	 * Returns the url of the page based on annotatin and the context setted
	 * @return Url based on the annotation setted on page
	 */
	public String getPageUrl() {
		
		if(getConfig() == null)
			throw new ArangiTesterException("Problema ao capturar a anotação");
		if(getConfig().url() == null)	
			throw new ArangiTesterException("URL da anotação está vazia");
		if(Context.getInstance() == null)
			throw new ArangiTesterException("Instancia está nula");
		if(Context.getInstance().getConfig() == null)
			throw new ArangiTesterException("getConfig da instancia está nula");
		if(Context.getInstance().getConfig().getPath() == null)
			throw new ArangiTesterException("getConfig.getPath da instancia está nula");
		
		String url = getConfig().url();
		if (!url.startsWith("/"))
			url = "/" + url;
		return Context.getInstance().getConfig().getPath() + url;
	}
}
