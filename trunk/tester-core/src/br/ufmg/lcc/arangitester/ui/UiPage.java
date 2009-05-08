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

import static br.ufmg.lcc.arangitester.Context.getInstance;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.Page;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.exceptions.InvokeException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.util.Refletions;
import br.ufmg.lcc.arangitester.util.ArangiTesterStringUtils;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

public class UiPage  extends UiComponent implements IUiComposite{
	/**
	 * In mileseconds
	 */
	public static String DEFAULT_PAGE_WAIT_TIME = "50000";
	
	/**
	 * Facilita a criação de uma pagina. Cria o proxy da página e chama a página inicial.
	 * @param <T> Alguma página.
	 * @param clazz Classe da página.
	 * @return Proxy da página.
	 */
	public static <T extends UiPage> T invoke(Class<T> clazz) {
		T page = UiComponentFactory.getInstance(clazz);
		page.invoke();
		return page;
	}
	/**
	 * Invoke url setted on @LccPage on the current class.
	 */
	public void invoke() {
		invoke(getPageUrl());
	}

	/**
	 * Invoke a url. Url must be Complete. Ex.: http://localhost:8080/app/test.faces
	 * To get http://localhost:8080/app from server.properties use LccContext.getInstance().getConfig().getPath()
	 */
	@Logger("Invoking Url: #0")
	public void invoke(String url){
		getSel().open(url);
		getSel().waitForPageToLoad(DEFAULT_PAGE_WAIT_TIME);
		
		//REVER
		// Pos-condição: verifica se a página foi carregada corretamente
		try{
			verifyUrl(url);
		}catch (Exception e) {
			throw new InvokeException("A pagina não foi carregada corretamente");
		}
	}
	
	/**
	 * Return a instance of interface Selenium. It is the only way for interaction with the browser.
	 */
	protected static Selenium getSel() {
		return getInstance().getSeleniumController().getSeleniumClient();
	}
	
	/**
	 * Closes the current page by clicking on the close button of the browser.
	 */
	@Logger("Closing page")
	public void closePage() {
		getSel().close();
		getSel().selectWindow(null);
	}
	
	@Logger("Verifying url: \"#0\" ")
	public void verifyUrl(String expectedUrl){
		String pageUrl = this.getBrowserUrl();

		if( !pageUrl.contains(expectedUrl) ){
			throw new ArangiTesterException("A url atual é diferente da esperada.\n" +
									"Url atual: " + pageUrl + "\n" +
									"Url esperada: " + expectedUrl);
		}
	}
	
	
	@Logger("Verifying alert: \"#0\" ")
	public void verifyAlert(String text){
		String alert = getSel().getAlert();
		if ( !ArangiTesterStringUtils.containsWithoutSpaces(text, alert) ){
			throw new ArangiTesterException("Mensagem de alerta esperada: " + text + " \nmas a atual é: " + text);
		}
	}
	
	/**
	 * Verify if text is present on page.
	 * @param expectedText text that must be on page.
	 */
	@Logger("Verificando Texto Presente: '#0'")
	public void verifyTextPresent(final String expectedText){
		final String locator = "xpath=//" + super.locator.getHtmlNameSpace() + "html";
		final String expectedTextWithoutSpaces = StringUtils.deleteWhitespace(expectedText);
		try{
			waitElement(locator);
			new Wait(){
				@Override
				public boolean until() {
					String html = getSel().getText(locator);
					html = StringUtils.deleteWhitespace(html);
					if ( !html.contains(expectedTextWithoutSpaces) ){
						return false;
					} else {
						return true;
					}
				}
			}.wait(locator, UiComponent.DEFAULT_ELEMENT_WAIT_TIME);
		}catch (WaitTimedOutException e){
			throw new TesterException("Texto esperado mas não existente: '" + expectedText +"'");
		}
	}

	/**
	 * Verify if text is NOT present on page.
	 * @param unExpectedText text that must NOT be on page.
	 */
	@Logger("Verificando Texto Ñ Presente: '#0'")
	public void verifyTextNotPresent(final String unExpectedText){
		final String locator = "xpath=//" + super.locator.getHtmlNameSpace() + "html";
		waitElement(locator);
		String html = getSel().getText(locator);
		if ( html.contains(unExpectedText) ) {
			throw new TesterException("Texto NÃO esperado mas existente: '" + unExpectedText +"'");
		}
	}

	/**
	 * Recursive call verifyPreviewslyAction in all UiComponents on this page.
	 */
	public void verifyAllPreviewslyActions(){
		Iterator<IUiComponent> fullIterator = fullIterator();
		while (fullIterator.hasNext()) {
			fullIterator.next().verifyPreviewslyAction();
		}
	}

	@SuppressWarnings("unchecked")
	public void verifyAllEnabled(boolean enabled, Class... components){
		for (IUiComponent component: this){
			if ( IUiComposite.class.isAssignableFrom(component.getClass()) ){
				((IUiComposite)component).verifyAllEnabled(enabled, components);
			} else{
				component.verifyIsEnable(enabled);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void verifyAllEnable(boolean enable) {
		Class[] components = new Class[]{UiInputText.class, UiCheckBox.class, UiTable.class, UiSelect.class};
		List<? extends IUiComponent> allUiComponents = Refletions.getAllUiComponents(this, components);
		for (IUiComponent component: allUiComponents){
			if ( IUiComposite.class.isAssignableFrom(component.getClass()) ){
				((IUiComposite)component).verifyAllEnable(enable);
			} else{
				if ( component.exist() )
					component.verifyIsEnable(enable);
			}
		}
	}

	/**
	 * Return LccPage annotated on subclass of LccUiPage
	 * @return null if not exist
	 */
	public Page getConfig(){
		return this.getClass().getSuperclass().getAnnotation(Page.class);
	}
	
	/**
	 * Returns the url of the page based on annotatin and the context setted
	 * @return Url based on the annotation setted on page
	 */
	public String getPageUrl() {
		String url = getConfig().url();
		if (!url.startsWith("/"))
			url = "/" + url;
		return Context.getInstance().getConfig().getPath() + url;
	}
	
	/**
	 * Returns the url of the page. Based on Selenium method getUrl().
	 */
	public String getBrowserUrl(){
		return getSel().getLocation();
	}
}
