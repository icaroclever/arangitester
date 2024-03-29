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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.arangitester.Context;
import org.arangitester.annotations.Logger;
import org.arangitester.annotations.RequestConfig;
import org.arangitester.annotations.Ui;
import org.arangitester.arangi.annotations.Page;
import org.arangitester.arangi.ui.Button;
import org.arangitester.exceptions.ElementNotExistException;
import org.arangitester.exceptions.TesterException;
import org.arangitester.log.IResult;
import org.arangitester.ui.IUiComponent;
import org.arangitester.ui.UiButton;
import org.arangitester.ui.UiDiv;
import org.arangitester.ui.UiPage;
import org.arangitester.ui.actions.IRequest;


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
	
	@Ui(desc="Close", locator="id=registerForm:buttonClose")
	@RequestConfig(window=IRequest.Window.CLOSE)
	private UiButton btnClose;
	
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
	@Override
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
		/*String pageUrl = this.getBrowserUrl();
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
		}*/
	}

	/**
	 * Verifyies if te url setted on the annotation of the page is compatible 
	 * with the url of this page. 
	 */
	public void verifyUrl(){
		//String expectedUrl = getPageUrl();
		//verifyUrl(expectedUrl);
	}

	/**
	 * Verify all buttons declared on LccPage(editButtons).
	 */
	@Logger("Verificando botões Edit Page")
	public void verifyEditButtons(){
		verifyButtons(getArangiConfig().editButtons());
	}

	/**
	 * Verify all buttons declared on LccPage(viewButtons).
	 */
	@Logger("Verificando botões View Page")
	public void verifyViewButtons(){
		verifyButtons(getArangiConfig().viewButtons());
	}

	/**
	 * Verify all buttons declared on LccPage(searchButtons).
	 */
	@Logger("Verificando botões Search Page")
	public void verifySearchButtons(){
		verifyButtons(getArangiConfig().searchButtons());
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
		}else if ( constant == Button.CLOSE ){
			return getBtnClose();
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
		return existButton(button, getArangiConfig().editButtons());
	}

	// TODO Documentação do método existSearchButton
	public boolean existSearchButton(Button button)
	{
		return existButton(button, getArangiConfig().searchButtons());
	}

	// TODO Documentação do método existViewButton
	public boolean existViewButton(Button button)
	{
		return existButton(button, getArangiConfig().viewButtons());
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
	@Override
	public String getPageTitle(){
		String title = "";
		String xpath = String.format("xpath=//%sspan[@class='pageTitle' or @class='titulo']", super.locator.getHtmlNameSpace());

		try{
			title = getSel().getText(xpath);
		} catch (SeleniumException e) {
			throw new ElementNotExistException("Esta página não possui título.\n" +
					"Locator utilizado para o título = " + xpath);
		}

		return title;
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
	
	public UiButton getBtnClose() {
		return btnClose;
	}

	public void setBtnClose(UiButton btnClose) {
		this.btnClose = btnClose;
	}

	/**
	 * Return Page annotated on subclass of UiPage
	 * @return null if not exist
	 */
	public Page getArangiConfig(){
		return this.getClass().getSuperclass().getAnnotation(Page.class);
	}	

	/**
	 * Returns the url of the page based on annotatin and the context setted
	 * @return Url based on the annotation setted on page
	 */
	@Override
	public String getPageUrl() {
		String url = getArangiConfig().url();
		if (!url.startsWith("/"))
			url = "/" + url;
		return Context.getInstance().getConfig().getPath() + url;
	}

}