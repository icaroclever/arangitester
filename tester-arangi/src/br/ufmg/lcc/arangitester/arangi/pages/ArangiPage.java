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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.arangi.annotations.Page;
import br.ufmg.lcc.arangitester.arangi.ui.Button;
import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.EnvException;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.log.IResult;
import br.ufmg.lcc.arangitester.ui.IRequest;
import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.ui.UiButton;
import br.ufmg.lcc.arangitester.ui.UiDiv;
import br.ufmg.lcc.arangitester.ui.UiPage;

import com.thoughtworks.selenium.SeleniumException;

/**
 * 
 * @author Lucas Gonçalves
 * 
 */
public class ArangiPage extends UiPage{

	private IResult log = Context.getInstance().getResult();
	
	//************************************************************************
	// Common UiComponents on Arangi Pages
	// 
	//************************************************************************
	@Ui(desc = "MessageArea", id = "messageId")
	private UiDiv messageArea;

	@Ui(desc="Print", locator="id=registerForm:buttonPrint")
	private UiButton btnPrint;

	@RequestConfig(confirmation=IRequest.Confirmation.OK)
	@Ui(desc="Delete", locator="id=registerForm:buttonDelete")
	private UiButton btnDelete;
	
	@Ui(desc="Save", locator="id=registerForm:buttonSave")
	private UiButton btnSave;
	
	@Ui(desc="New", locator="id=registerForm:buttonNew")
	private UiButton btnNew;

	@Ui(desc="Search", locator="id=registerForm:buttonSearch")
	private UiButton btnSearch;

	@Ui(desc="Search (Open)", locator="id=registerForm:buttonOpen")
	private UiButton btnOpen;
	
	@Ui(desc="Cancel", locator="id=registerForm:buttonCancel")
	private UiButton btnCancel;

	@Ui(desc="Edit", locator="id=registerForm:buttonEdit")
	private UiButton btnEdit;
	
	/**
	 * Invoke url to modify a registry already in database based on url setted on @Page on the current class.
	 * @param id of registry. To discovery id, go to search page, stop mouse over the magnifying glass and look on status bar
	 * of browser for the attribute id on url.
	 * 
	 */
	@Logger("Url: #{pageUrl} modify #0")
	public void invokeModify(int id) {
		invoke(getPageUrl() + "?event=edit&id=" + id);
	}

	/**
	 * Invoke url setted on @Page on the current class.
	 */
	public void invoke() {
		invoke(getPageUrl());
	}

	/**
	 * Invoke url to VIEW a registry already in database based on url setted on @Page on the current class.
	 * @param id of registry. To discovery id, go to search page, stop mouse over the magnifying glass and look on status bar
	 * of browser for the attribute id on url.
	 * 
	 */
	@Logger("Url: #{pageUrl} View #0")
	public void invokeView(int id) {
		invoke(getPageUrl() + "?event=view&id=" + id);
	}
	
	/**
	 * Verifyies if te url passed as param is compatible with the url of the page 
	 */
	@Override
	@Logger("Verifying url: \"#0\" ")
	public void verifyUrl(String expectedUrl){
		String pageUrl = this.getBrowserUrl();
		String expectedUrlBuckup = new String(expectedUrl);
		
		// Tries to get only the page part of the string. Like Process.faces or EtlSearch.faces 
		try{
			expectedUrl = expectedUrl.substring(expectedUrl.lastIndexOf('/') + 1);
			expectedUrl = expectedUrl.substring(0, expectedUrl.lastIndexOf(".faces") + 6);
		}catch (Exception e) {
			expectedUrl = expectedUrlBuckup; 
		}

		if( !pageUrl.contains(expectedUrl) ){
			throw new ArangiTesterException("A url atual é diferente da esperada.\n" +
									"Url atual: " + pageUrl + "\n" +
									"Url esperada: " + expectedUrl);
		}
	}
	
	/**
	 * Verifyies if te url setted on the annotation of the page is compatible 
	 * with the url of this page. 
	 */
	public void verifyUrl(){
		String expectedUrl = getPageUrl();
		verifyUrl(expectedUrl);
	}
	
	/**
	 * Verify all buttons declared on LccPage(editButtons).
	 */
	@Logger("Verificando botões Edit Page")
	public void verifyEditButtons(){
		verifyButtons(getConfig().editButtons());
	}
	
	/**
	 * Verify all buttons declared on LccPage(viewButtons).
	 */
	@Logger("Verificando botões View Page")
	public void verifyViewButtons(){
		verifyButtons(getConfig().viewButtons());
	}
	
	/**
	 * Verify all buttons declared on LccPage(searchButtons).
	 */
	@Logger("Verificando botões Search Page")
	public void verifySearchButtons(){
		verifyButtons(getConfig().searchButtons());
	}
	
	public void verifyButtons(Button[] present) {
		for ( Button p: present ){
			log.addInfo("Verificando botão esperado: " + p.toString() );
			if ( !getButton(p).exist() ){
				throw new ElementNotExistException("Botão " + p + " não existe!");
			}
		}
		
		List<Button> all = new ArrayList<Button>();
		Collections.addAll(all, Button.values());
		for ( Button p: present ){
			all.remove(p);
		}
		
		for ( Button p: all){
			log.addInfo("Verificando botão não esperado: " + p.toString() );
			if ( getButton(p).exist() ){
				throw new ElementNotExistException("Botão " + p + " presente na tela, mas não deveria!");
			}
		}
	}
	
	private UiButton getButton(Button constant){
		if ( constant == Button.NEW ){
			return getBtnNew();
		}else if ( constant == Button.DELETE ){
			return getBtnDelete();
		}else if ( constant == Button.OPEN ){
			return getBtnOpen();
		}else if ( constant == Button.SEARCH ){
			return getBtnSearch();
		}else if ( constant == Button.CANCEL ){
			return getBtnCancel();
		}else if ( constant == Button.SAVE ){
			return getBtnSave();
		}else if ( constant == Button.EDIT ){
			return getBtnEdit();
		}else if ( constant == Button.PRINT ){
			return getBtnPrint();
		}
		return null;
	}
	
	// TODO Documentação do método existButton
	
	private boolean existButton(Button button,Button[] present)
	{
		for ( Button p: present){
			if (p == button){
				return true;
			}
		}
		return false;
	}
	
	// TODO Documentação do método existEditButton
	public boolean existEditButton(Button button)
	{
		return existButton(button, getConfig().editButtons());
	}
	
	// TODO Documentação do método existSearchButton
	public boolean existSearchButton(Button button)
	{
		return existButton(button, getConfig().searchButtons());
	}
	
	// TODO Documentação do método existViewButton
	public boolean existViewButton(Button button)
	{
		return existButton(button, getConfig().viewButtons());
	}
	
	/**
	 * Verify if a message is present on page.
	 * It's important to notice that it compares the expected message with the 
	 * message presented removing whitespaces from both texts.
	 * @param expectedMessage text that must be on the Message Area.
	 */
	@Logger("Verificando Mensagem: '#0'")
	public void verifyMessagePresent(final String expectedMessage){
		getMessageArea().verifyTextInsideWithoutLog(expectedMessage);
	}
	
	/**
	 * Verify if a message is NOT present on the page message area.
	 * @param unExpectedMessage
	 */
	@Logger("Verificando Mensagen NÃO presente '#0'.")
	public void verifyMessageNotPresent(final String unExpectedMessage) {
		if( getMessageArea().isTextInsidePresentWithOutWait(unExpectedMessage) ) {
			throw new TesterException("Mensagem NÃO esperada mas existente: '" + unExpectedMessage +"'");
		}
	}
	
	/**
	 * Get the title of the page.
	 * @return The title, like Pesquisa de Processamento, or Repositorio de Dados, etc. 
	 */
	public String getTitle(){
		String title = "";
		String xpath = String.format("xpath=//%sspan[@class='title' or @class='titulo']", super.locator.getHtmlNameSpace());
		
		try{
			title = getSel().getText(xpath);
		} catch (SeleniumException e) {
			throw new ElementNotExistException("Esta página não possui título.\n" +
												"Locator utilizado para o título = " + xpath);
		}
		
		return title;
	}
	
	
	/**
	 * Verifyes the Arangi title of the page.
	 * A blank String is accepted as param too, and will be compared to the
	 * title of the page.
	 * A null param is not accepted, and a exception will be thrown. 
	 * @param expectedTitle
	 */
	@Logger("Verifying title: #0")
	public void verifyTitle(String expectedTitle){
		if(expectedTitle==null) throw new EnvException("Parametro expectedTitle não pode ser nulo!");
		
		String title = getTitle();
		if(!expectedTitle.equals(title))
			throw new WrongValueException("O título da página não corresponde ao esperado." +
					"\nTítulo da página: " + title +
					"\nTítulo esperado: " + expectedTitle);
	}
	
	/**
	 * Recursive call clearPreviewslyAction in all UiComponents on this page.
	 */
	public void clearAllPreviewslyActions(){
		Iterator<IUiComponent> fullIterator = fullIterator();
		while (fullIterator.hasNext()) {
			fullIterator.next().clearPreviewslyAction();
		}
	}

	public UiButton getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(UiButton btnSave) {
		this.btnSave = btnSave;
	}

	
	public UiButton getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(UiButton btnDelete) {
		this.btnDelete = btnDelete;
	}

	public UiButton getBtnNew() {
		return btnNew;
	}

	public void setBtnNew(UiButton btnNew) {
		this.btnNew = btnNew;
	}

	public UiButton getBtnSearch() {
		return btnSearch;
	}

	public void setBtnSearch(UiButton btnSearch) {
		this.btnSearch = btnSearch;
	}

	public UiButton getBtnOpen() {
		return btnOpen;
	}

	public void setBtnOpen(UiButton btnOpen) {
		this.btnOpen = btnOpen;
	}

	public UiButton getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(UiButton btnCancel) {
		this.btnCancel = btnCancel;
	}

	public UiButton getBtnEdit() {
		return btnEdit;
	}

	public void setBtnEdit(UiButton btnEdit) {
		this.btnEdit = btnEdit;
	}

	public UiDiv getMessageArea() {
		return messageArea;
	}

	public void setMessageArea(UiDiv messageArea) {
		this.messageArea = messageArea;
	}

	
	public UiButton getBtnPrint() {
		return btnPrint;
	}

	public void setBtnPrint(UiButton btnPrint) {
		this.btnPrint = btnPrint;
	}
	
	/**
	 * Return Page annotated on subclass of UiPage
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

}