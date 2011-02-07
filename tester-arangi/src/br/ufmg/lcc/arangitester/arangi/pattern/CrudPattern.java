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
package br.ufmg.lcc.arangitester.arangi.pattern;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Test;
import br.ufmg.lcc.arangitester.arangi.annotations.Crud;
import br.ufmg.lcc.arangitester.arangi.annotations.Field;
import br.ufmg.lcc.arangitester.arangi.annotations.Page;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiSearchPage;
import br.ufmg.lcc.arangitester.arangi.pages.NullSearchPage;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiPage;
import br.ufmg.lcc.arangitester.arangi.ui.Button;
import br.ufmg.lcc.arangitester.arangi.ui.GenericLine;
import br.ufmg.lcc.arangitester.exceptions.EnvException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.ui.UiInputText;
import br.ufmg.lcc.arangitester.ui.actions.IRequest;

/**
 * Contains many commons test methods for Arangi pattern CRUD, Master Detail and Search.
 * For use these test methods subclass this class and gives configurations to it using LccCrud.
 * 
 * @see Crud
 * @author Lucas Gonçalves
 *
 */
public class CrudPattern extends BasePatterns{
	
	private Crud config = getCrudNotation();
	
	protected Crud getCrudNotation(){
		
		if(config == null)
		{
			config = this.getClass().getAnnotation(Crud.class);
			if(config==null){
				throw new EnvException("\nEsta classe - "+ this.getClass().getName() +" - não " +
						"possui a notação Crud, necessária para correta execução do CrudPattern.\n" +
						"Crie a anotação ou então não herde a classe LccCrudPattern. ");
			}
		}
		
		return config;
	}
	
	
	@Override
	public boolean isToExecute(String methodName) {
		if ( "verifyDependencyBetweenEntities".equals(methodName) ){
			if ( StringUtils.isBlank(config.dependencyMessage()) ){
				return false;
			}
		}else if ("verifyDuplicity".equals(methodName)){
			if ( StringUtils.isBlank(config.duplicityMessage()) ){
				return false;
			}
		}else if ( "verifyRequiredFields".equals(methodName)){
			if ( StringUtils.isBlank(config.messageRequiredFields()) ){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Template method. Override this method to do an action after call the SearchPage,
	 * on method verifyRedirects().
	 * 
	 * @see #verifyRedirects()
	 * @param searchPage
	 */
	protected void afterCallSearchPage(ArangiSearchPage searchPage){}
	
	/**
	 * Template method. Override this method to do an action after call the EditPage,
	 * on method verifyRedirects().
	 * 
	 * @see #verifyRedirects()
	 * @param searchPage
	 */
	protected void afterCallEditPage(ArangiPage page){}
	
	/**
	 * <H2>Verifica fluxo de redirecionamento dos botões.</H2> 
	 *Testa os seguintes fluxos de execução:
	 *<ul>
	 *	<li> novo->cancelar;
	 *  <li> novo->salvar->pesquisar; (só deve ter botão pesquisar e imprimir depois de salvar)
	 *  <li> novo->salvar->editar->salvar;
	 *  <li> novo->salvar->pesquisar;
	 *  <li> alterar->cancelar (deve voltar para página de pesquisa)
	 *  <li> alterar->salvar->pesquisar; (só deve ter pesquisar e imprimir)
	 *  <li> visualizar->novo->cancelar;
	 *  <li> entrar->pesquisar;   
	 *</ul>
	 */
	@Test(value="Valida os redirecionamentos", order=1)
	public void verifyRedirects(){
		if(config.page() == null || config.searchPage() == null){
			throw new EnvException("As classes de mapeamento da página não possuem as anotações necessárias." +
					"\nConfira as classes de mapeamento - *page e *SearchPage, e verifique as anotações LccCrud.");
		}
		
		// TODO Organizar a verificação de título
		//String title;	// The arrange title of current page
		ArangiPage editPage = createPage(config.page(), "page"); //edit page
		ArangiPage controller; //controller page of this test. Can be search or edit page.
		
		// Sets the RequestConfig of the cancel button of edit page
		if(editPage.existEditButton(Button.CANCEL))
			editPage.getBtnCancel().getRequestConfig().setConfirmation(IRequest.Confirmation.OK);
		
		if(config.searchPage() != NullSearchPage.class)
		{
			ArangiPage searchPage = createPage(config.searchPage(), "page"); //searchPage
			// novo->cancelar;
			controller = searchPage;
			controller.invoke();
			if(controller.existSearchButton(Button.NEW)){
				controller.getBtnNew().click(); //Should redirect the browser to edit page.
				
				controller = editPage; // Change of page
				afterCallEditPage(controller);
				controller.verifyUrl();
			//	title = controller.getArangiConfig().includeTitle();
				//if(!StringUtils.isBlank(title)) controller.verifyPageTitle(title);
				
				if(controller.existSearchButton(Button.CANCEL))
				{
					controller.getBtnCancel().click(); //Should redirect the browser to SearchPage
				
					controller = searchPage; // Change of page
					controller.verifyUrl();
				//	title = controller.getArangiConfig().searchTitle();
					//if(!StringUtils.isBlank(title)) controller.verifyPageTitle(title);
				}
			}
		}
		else
		{
			editPage.invoke();
			controller = editPage; // Change of page
			afterCallEditPage(controller);
			controller.verifyUrl();
			//title = controller.getArangiConfig().includeTitle();
			//if(!StringUtils.isBlank(title)) controller.verifyPageTitle(title);
		}
	}
	
	/**
	 * Call search page and call validate to all fields. To configure field validation see documetations
	 * for each UiComponent. Most common validation is implemented in LccUiInputText.
	 */
	@Test(value="Valida os campos da página de Pesquisa", order=2)
	public void validateSearchPage(){
		
		if(config.searchPage() == NullSearchPage.class)
		{
			Context.getInstance().getResult().addInfo("Página de pesquisa não existente para o Caso de Uso.");
			return;
		}
		ArangiSearchPage searchPage = createPage(config.searchPage(), "search");
		
		searchPage.invoke();
		
		beforeValidateSearchPage(searchPage);
		
		// Valida o título da página de pesquisa
		/*if(StringUtils.isNotBlank(searchPage.getArangiConfig().searchTitle())){
			searchPage.verifyPageTitle(searchPage.getArangiConfig().searchTitle());
		}*/
		
		Iterator<IUiComponent> lccFullComponentIterator = searchPage.singleWithClosedTableItemsIterator();
		while (lccFullComponentIterator.hasNext()) {
			IUiComponent next = lccFullComponentIterator.next();
			beforeValidateField(searchPage, next);
			next.validate(searchPage.getBtnSearch(), searchPage.getMessageArea());
			afterValidateField(searchPage, next);
		}
	}
	
	/**
	 * Template Method
	 * 
	 * @see #beforeValidateEditPage()
	 * @param page to validate
	 */
	public void beforeValidateSearchPage( ArangiSearchPage searchPage ) {
		
	}
	
	/**
	 * Template Method
	 * 
	 * @see #beforeValidateSearchPage()
	 * @param page to validate.
	 */
	public void beforeValidateEditPage( ArangiPage page ) {
		
	}
	
	/**
	 * @see #validateSearchPage()
	 */
	@Test(value="Valida os campos da página de Edição", order=3)
	public void validateEditPage(){
		ArangiPage page = createPage(config.page(), "page");
		page.invoke();
		
		beforeValidateEditPage(page);
		
		// Valida o título da página de edição
		/*if(StringUtils.isNotBlank(page.getArangiConfig().includeTitle())){
			page.verifyPageTitle(page.getArangiConfig().includeTitle());
		}*/
		Iterator<IUiComponent> lccFullComponentIterator = page.singleWithClosedTableItemsIterator();
		while (lccFullComponentIterator.hasNext()) {
			IUiComponent next = lccFullComponentIterator.next();
			beforeValidateField(page, next);
			next.validate(page.getBtnSave(), page.getMessageArea());
			afterValidateField(page, next);
		}
	}
	
	/**
	 * Call search, edit and view page and verify presence of buttons setted on {@link Page} 
	 * on search page and edit page.
	 */
	@Test(value="Verificar botões", order=4)
	public void verifyButtons(){
		
		if(config.searchPage() != NullSearchPage.class){
			ArangiSearchPage searchPage = createPage(config.searchPage(), "search");
			searchPage.invoke();
			searchPage.verifySearchButtons();
		}
		
		ArangiPage page = createPage(config.page(), "page");
		page.invoke();
		page.verifyEditButtons();
		
		if(config.searchPage() != NullSearchPage.class && config.modifyId()>0){
			page.invokeView(config.modifyId());
			page.verifyViewButtons();
		}
	}
	
	/**
	 * Open new registry. Click on save button and verify message on {@link Crud#messageRequiredFields()}.
	 * Only execute if template method {@link #beforeRequiredFields(ArangiPage)} returns true.
	 */
	@Test(value="Verificar Campos Obrigatórios", order=5)
	public void verifyRequiredFields(){
		//TODO Esse método não verifica cada campo obrigatório para verificar se ele estando nulo está ok
		ArangiPage page = createPage(config.page(), "page");
		page.invoke();
		beforeRequiredFields(page);
		page.getBtnSave().click();
		page.verifyMessagePresent(config.messageRequiredFields());
	}
	
	/**
	 * Template Method
	 * 
	 * @see #verifyRequiredFields()
	 * @param page to verify.
	 */
	protected void beforeRequiredFields( ArangiPage page ){
	}
	
	/**
	 * Verify dependency between entities in the system. It calls the search page,
	 * then it tries to delete the element corresponding to the id passed by
	 * {@link Crud#dependencyId()}, checking the dependency error message, passed
	 * by {@link Crud#dependencyMessage()}. The id must correspond to an 
	 * referenced element, that means, the element must be referenced by another
	 * entity in the system, and therefore, it shouldn't be possible to be deleted.
	 * If {@link Crud#dependencyMessage()} is not informed, this method does
	 * nothing.
	 */
	@Test(value="Verificar Dependência entre entidades", order=6)
	public void verifyDependencyBetweenEntities(){
		int linePosition;
		
		if(config.searchPage()==NullSearchPage.class){
			Logger.getLogger(CrudPattern.class).info("Dependências de Entidades não verificadas por não haver tela de Pesquisa.");
			return;
		}
		if(config.dependencyId() != 0){
			ArangiSearchPage searchPage = createPage(config.searchPage(), "search");
			searchPage.invoke();
			searchPage.getBtnSearch().click();
			linePosition = searchPage.getLinePositionFromRegistryId(config.dependencyId());
			searchPage.getResult().getLine(linePosition).getCheckDelete().check();
			searchPage.getBtnDelete().click();
			searchPage.verifyMessagePresent(config.dependencyMessage());
		}
	}
	
	/**
	 * Call new registry page. Fill fields with {@link Field#addValue()} values, save, call search page
	 * fill fields with same with same values just added click on the first result and verify if fields are
	 * filled with addValue().
	 * Names of fields on page class must be the same as search page.
	 * For example:
	 * <code>
	 * 		public {@link UiInputText} name;
	 * </code>
	 * name must be the same on search and creation page.
	 */
	@Test(value="Adicionar Registro", order=7)
	public void addRegistry(){
		ArangiPage page = createPage(config.page(), "page");
		page.invoke();
		beforeAddRegistry(page);
		for (Field field: config.fields()){
			IUiComponent fieldOnPage = resolveElExpression(field.name(), "page");
			if ( fieldOnPage == null ){
				throw new TesterException("O componente " + field.name() + " não existe na página " + config.page().getSimpleName());
			}
			beforeFillFieldAddRegistry(page, field);
			fill(fieldOnPage, new FieldImpl(field), ACTION.ADD);
			afterFillFieldAddRegistry(page, field);
		}
		
		page.getBtnSave().click();
		page.verifyMessagePresent(config.saveMessage());
	}
	
	public void beforeAddRegistry(ArangiPage page)
	{
	}
	
	/**
	 * Fill fields with {@link Field#addValue()}, click on save button and verify message {@link Crud#duplicityMessage()}.
	 * It will be executed only if {@link Crud#duplicityMessage()} was setted.
	 */
	@Test(value="Verificando duplicidade", dependency="addRegistry", order=8)
	public void verifyDuplicity(){
		ArangiPage page = createPage(config.page(), "page");
		page.invoke();

		for (Field field: config.fields()){
			fill(resolveElExpression(field.name(), "page"), new FieldImpl(field), ACTION.ADD);
		}

		page.getBtnSave().click();
		page.verifyMessagePresent(config.duplicityMessage());
	}

	/**
	 * Template Method. Execute before validate a field.
	 */
	public void beforeValidateField(ArangiPage page, IUiComponent component){
	}
	
	/**
	 * Template Method. Execute after validate field.
	 */
	public void afterValidateField(ArangiPage page, IUiComponent next){
	}
	
	/**
	 * Template Method. Execute before fill a field on AddRegistry Test.
	 */
	public void beforeFillFieldAddRegistry(ArangiPage page, Field field){
	}
	
	/**
	 * Template Method. Execute after fill a field on AddRegistry Test.
	 */
	public void afterFillFieldAddRegistry(ArangiPage page, Field field){
	}

	
	/**
	 * Call modify page on registry {@link Crud#modifyId()} backup original values, fill fields with {@link Field#modifyValue()},
	 * click on CANCEL button, call modify page again and verify backed up values are there.
	 */
	@Test(value="Verificando cancelamento de Registro", order=9)
	public void verifyCancelRegister(){
		
		if(config.modifyId() < 0)
		{
			Context.getInstance().getResult().addObs("Modificação de Registro não existe no Caso de Uso.");
			return;
		}
		ArangiPage page = createPage(config.page(), "page");
		ACTION action;
		if(config.searchPage() == NullSearchPage.class){
			page.invoke();
			action = ACTION.ADD;
		}
		else{
			page.invokeModify(config.modifyId());
			action = ACTION.MODIFY;
		}
	
		ArrayList<String> backupOriginalValues = new ArrayList<String>();
		int i=0;
		for (Field field: config.fields()){
			IUiComponent target = resolveElExpression(field.name(), "page");
			if( target != null ) {
				backupOriginalValues.add( getValue(target));
			}
		}
		for (Field field: config.fields()){
			IUiComponent target = resolveElExpression(field.name(), "page");
			if ( beforeFillFieldCancelRegistry(page, field)){
				fill(target, new FieldImpl(field), action);
				afterFillFieldCancelRegistry(page, field);
			}
		}
		page.getBtnCancel().getRequestConfig().setConfirmation(IRequest.Confirmation.OK);
		page.getBtnCancel().click();
		
		if(config.searchPage() == NullSearchPage.class){
			page.invoke();
		}
		else{
			page.invokeModify(config.modifyId());
		}
		i=0;
		for (Field field: config.fields()){
			IUiComponent target = resolveElExpression(field.name(), "page");
			
			if(!backupOriginalValues.isEmpty())
				verify(target, backupOriginalValues.get(i++));
		}
	}
	
	/**
	 * Template Method. Execute before fill a field on AddRegistry Test.
	 */
	public boolean beforeFillFieldCancelRegistry(ArangiPage page, Field field){
		return true;
	}
	
	/**
	 * Template Method. Execute after fill a field on AddRegistry Test.
	 */
	public void afterFillFieldCancelRegistry(ArangiPage page, Field field){
	}
	
	/**
	 * Call modify page on registry {@link Crud#modifyId()}, fill fields with {@link Field#modifyValue()},
	 * save, call modify page again and verify modified values.
	 * Template methods {@link #beforeFillFieldModifyRegistry(ArangiPage, Field)} and {@link #afterFillFieldModifyRegistry(ArangiPage, Field)}
	 */
	@Test(value="Modificar Registro", order=10)
	public void modifyRegistry(){
		
		if(config.searchPage() == NullSearchPage.class)
		{
			Context.getInstance().getResult().addObs("Página de Pesquisa não Informada.");
			return;
		}
		
		if(config.modifyId() < 0)
		{
			Context.getInstance().getResult().addObs("Modificação de Registro não existe no Caso de Uso.");
			return;
		}
		
		ArangiPage page = createPage(config.page(), "page");
		page.invokeModify(config.modifyId());
		
		for (Field field: config.fields()){ //Must be separated. Because dynamic combobox
			IUiComponent target = resolveElExpression(field.name(), "page");
			
			if ( beforeFillFieldModifyRegistry(page, field) ){
				fill(target, new FieldImpl(field), ACTION.MODIFY);
				afterFillFieldModifyRegistry(page, field);
			}
		}
		
		page.getBtnSave().click();
		page.verifyMessagePresent(config.saveMessage());
		
		page.invokeModify(config.modifyId());

		for (Field field: config.fields()){
			IUiComponent fieldOnPage = resolveElExpression(field.name(), "page");
			if(field.noVerify()){
				return;
			}
			if ( fieldOnPage == null ){
				throw new TesterException("O componente " + field.name() + " não existe na página " + config.page().getSimpleName());
			}
			fieldOnPage.verifyPreviewslyAction();
		}
	}
	
	/**
	 * Template Method. Execute before fill a field on modifyRegistry Test.
	 */
	public boolean beforeFillFieldModifyRegistry(ArangiPage page, Field field){
		return true;
	}
	
	/**
	 * Template Method. Execute after fill a field on modifyRegistry Test.
	 */
	public void afterFillFieldModifyRegistry(ArangiPage page, Field field){
	}
	
	/**
	 * Call search page. Fill fields with {@link Field#addValue()} mark line {@link Crud#searchLine()},
	 * click on delete button. Verify message {@link Crud#deleteMessage()}.
	 * 
	 * Fill fields with {@link Field#addValue()} and verify if the number of lines is one unit less than
	 * it was before.
	 */
	@Test(value="Apagar Registro", dependency="addRegistry", order=11)
	public void deleteRegistry(){
		
		if(config.searchPage() == NullSearchPage.class)
		{
			Context.getInstance().getResult().addObs("Remoção de registro não existe no Caso de Uso");
			return;
		}
		
		ArangiSearchPage searchPage = createPage(config.searchPage(), "search");
		searchPage.invoke();
		
		for (Field field: config.fields()){
			IUiComponent target = resolveElExpression(field.name(), "search");
			if (target != null){
				fill(target, new FieldImpl(field), ACTION.ADD);
			}
		}
		
		searchPage.getBtnSearch().click();
		int bodyLines = searchPage.getResult().getRealLinesNumber();

		GenericLine lineToVerify = null;
		if(!config.searchLineValue().equals(""))
		{
			(lineToVerify = searchPage.getResult().getLineFromContent(config.searchLineValue())).getCheckDelete().check();
		}
		if(lineToVerify ==null)
			searchPage.getResult().getLine(config.searchLine()).getCheckDelete().check();
		
		
		searchPage.getBtnDelete().click();
		searchPage.verifyMessagePresent(config.deleteMessage());
		
		searchPage.invoke();
		
		for (Field field: config.fields()){
			IUiComponent target = resolveElExpression(field.name(), "search");
			if (target != null){
				fill(target, new FieldImpl(field), ACTION.ADD);
			}
		}
		searchPage.verifyResult( bodyLines - 1 );
	}
	
	@SuppressWarnings("unused")
	private GenericLine getResultLineFromAddedRegistry(ACTION action){
		ArangiSearchPage searchPage = createPage(config.searchPage(), "search");
		searchPage.invoke();
		
		for (Field field: config.fields()){
			IUiComponent target = resolveElExpression(field.name(), "search");
			if (target != null){
				fill(target, new FieldImpl(field), action);
			}
		}
		
		searchPage.getBtnSearch().click();
		return searchPage.getResult().getLine(config.searchLine());
	}
}
