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

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Test;
import br.ufmg.lcc.arangitester.arangi.annotations.Crud;
import br.ufmg.lcc.arangitester.arangi.annotations.Field;
import br.ufmg.lcc.arangitester.arangi.annotations.Tabular;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiTabularPage;
import br.ufmg.lcc.arangitester.arangi.ui.GenericLine;
import br.ufmg.lcc.arangitester.ui.IUiComponent;

/**
 * Contains many commons test methods for Arangi pattern Tabular.
 * For use these test methods subclass this class and gives configurations to it using LccTabular.
 * 
 * @see Tabular
 * @author Lucas Gonçalves
 *
 */
public class TabularPattern extends BasePatterns{
	private final Tabular config = this.getClass().getAnnotation(Tabular.class);

	@Test(value="Verificando cancelamento de Registro", order=1)
	public void verifyCancelRegister(){
		if ( !config.editAll() ){
			ArangiTabularPage<? extends GenericLine> page = createPage(config.page(), "page");
			page.invoke();
			page.getLine(0).getModify().click();

			String[] backupOriginalValues = new String[config.fields().length];

			int i=0;
			for (Field field: config.fields()){
				IUiComponent fieldOnPage = resolveElExpression("table[0]." +field.name(), "page");
				backupOriginalValues[i++] = getValue(fieldOnPage);
				fill(fieldOnPage, field, ACTION.CANCEL);
			}
			page.getLine(0).getCancel().click();

			i=0;
			for (Field field: config.fields()){
				IUiComponent fieldOnPage = resolveElExpression("table[0]." +field.name(), "page");
				verify(fieldOnPage, backupOriginalValues[i++]);
			}
		}
	}

	/**
	 * Verify dependency between entities in the system. It calls the tabular page
	 * and then tries to delete the element corresponding to the line passed by
	 * LccTabular#dependencyLineNumber(), checking the dependency error message,
	 * passed by LccTabular#dependencyMessage(). The line must correspond to an
	 * referenced element, that means, the element must be referenced by another
	 * entity in the system, and therefore, it shouldn't be possible to be deleted.
	 * If LccTabular#dependencyMessage() is not informed, this method does
	 * nothing.
	 * 
	 * @see Tabular
	 */
	@Test(value="Verificar Dependência entre entidades", order=2)
	public void verifyDependencyBetweenEntities(){
		if ( StringUtils.isNotBlank(config.dependencyMessage())){
			ArangiTabularPage<? extends GenericLine> page = createPage(config.page(), "page");
			page.invoke();
			page.getLine(config.dependencyLineNumber()).getCheckDelete().check();
			page.getBtnDelete().click();
			page.verifyMessagePresent(config.dependencyMessage());

		}else{
			Context.getInstance().getResult().addInfo("Dependencia não verificada.");
		}
	}

	@Test(value="Adicionar Registro", order=3)
	public void addRegistry(){
		ArangiTabularPage<? extends GenericLine> page = createPage(config.page(), "page");
		page.invoke();

		int lineToAdd = 0;
		if ( !config.addFirst() ){
			lineToAdd = page.getTable().getRealLinesNumber();
		}
		page.getBtnNew().click();
		if ( config.addFirst() ) {
			page.addLine(true);
		}

		for (Field field: config.fields()){
			IUiComponent fieldOnPage = resolveElExpression("table[" + lineToAdd + "]." +field.name(), "page");
			fill(fieldOnPage, field, ACTION.ADD);
		}

		if ( config.editAll() ) {
			page.getBtnSave().click();
		}else{
			page.getLine(lineToAdd).getSave().click();
		}
		page.verifyMessagePresent(config.saveMessage());

		/* When arangi save a registry it can either put the new registry at the last line or
		 * order all registries. The programmer have to know when each one of this situations
		 * happens, because the behaviour can be different between distinct pages in the same
		 * application. By default, it is considered that all registry saved are placed at
		 * the last line. When this is not true, the value of "indexOrderedAfterSave" has to
		 * be the ID of the new registry that has just been saved.
		 */
		page.invoke();
		if( config.indexOrderedAfterSave() == -1 ) {
			lineToAdd = page.getTable().getRealLinesNumber() - 1;
		} else {
			lineToAdd = config.indexOrderedAfterSave();
		}
		for (Field field: config.fields()) {
			IUiComponent fieldOnPage = resolveElExpression("table[" + lineToAdd + "]." +field.name(), "page");
			verify(fieldOnPage, field.addValue());
		}

	}

	@Test(value="Modificar Registro", order=4)
	public void modifyRegistry(){
		if ( !config.modifyRegistry() ){
			Context.getInstance().getResult().addInfo("Não é necessário modificar registro");
			return;
		}

		ArangiTabularPage<? extends GenericLine> page = createPage(config.page(), "page");
		page.invoke();

		GenericLine lineToModify = page.getLine(config.modifyRegisterNumber());
		if(!config.editAll()){
			lineToModify.getModify().click();
		}

		String[] backupOriginalValues = new String[config.fields().length];
		int i=0;
		for (Field field: config.fields()){
			IUiComponent fieldOnPage = resolveElExpression("table[" + config.modifyRegisterNumber() + "]." +field.name(), "page");
			backupOriginalValues[i++] = getValue(fieldOnPage);
			fill(fieldOnPage, field, ACTION.MODIFY);
		}

		if (config.editAll()){
			page.getBtnSave().click();
		}else{
			lineToModify.getSave().click();
		}

		page.verifyMessagePresent(config.modifyMessage());
	}

	/**
	 * Fill fields with {@link Field#addValue()}, click on save button and verify message {@link Crud#duplicityMessage()}.
	 * It will be executed only if {@link Crud#duplicityMessage()} was setted.
	 */
	@Test(value="Verificando duplicidade", dependency="addRegistry", order=5)
	public void verifyDuplicity(){
		if ( StringUtils.isNotBlank(config.duplicityMessage()) ){
			ArangiTabularPage<? extends GenericLine> page = createPage(config.page(), "page");
			page.invoke();

			int lineToAdd = 0;
			if ( !config.addFirst() ){
				lineToAdd = page.getTable().getRealLinesNumber();
			}
			page.getBtnNew().click();
			if ( config.addFirst() ) {
				page.addLine(true);
			}

			for (Field field: config.fields()){
				IUiComponent fieldOnPage = resolveElExpression("table[" + lineToAdd + "]." +field.name(), "page");
				fill(fieldOnPage, field, ACTION.ADD);
			}

			if ( config.editAll() ) {
				page.getBtnSave().click();
			}else{
				page.getLine(lineToAdd).getSave().click();
			}
			page.verifyMessagePresent(config.duplicityMessage());
		}else{
			Context.getInstance().getResult().addInfo("Não existe verificação de duplicidade.");
		}
	}

	@Test(value="Apagar Registro", dependency="addRegistry", order=6)
	public void deleteRegistry(){
		ArangiTabularPage<? extends GenericLine> page = createPage(config.page(), "page");
		page.invoke();

		int addedLine = page.getTable().getRealLinesNumber() - 1;

		page.getLine(addedLine).getCheckDelete().check();
		if( page.getBtnDelete().exist() ) {
			page.getBtnDelete().click();
			page.verifyMessagePresent(config.deleteMessage());
		} else{
			page.getBtnSave().click();
			page.verifyMessagePresent(config.saveMessage());
		}
	}
}
