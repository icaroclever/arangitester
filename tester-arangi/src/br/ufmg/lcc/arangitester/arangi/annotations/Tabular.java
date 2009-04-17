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
package br.ufmg.lcc.arangitester.arangi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.ufmg.lcc.arangitester.arangi.pages.ArangiTabularPage;
import br.ufmg.lcc.arangitester.ui.GenericLine;

/**
 * 	-Verificar cancelar registro
 *		+ Chama pagina
 *		+ Clica em modificar[0]
 *		+ Escreve LccField.cancelValue() no campo
 *		+ Clica em cancelar
 *		+ Verifica o valor anterior original
 *	-Adicionar registro
 *		+ Chama pagina
 *		+ Clica Botão novo
 *		+ Preenche o campo LccField.addValue()
 *		+ Clica em Salvar
 *		+ Verifica texto saveMessage()
 *		+ Chama pagina
 *		+ Verifica valor (ultima linha ou o ID passado no atributo indexOrderedAfterSave)
 *	-Modificar Registro
 *		+ Chama pagina
 *		+ Clica em Modificar Registro[modifyRegisterNumber()]
 *		+ Altera o valor do campo com LccField.modifyValue()
 *		+ Clica em salvar
 *		+ Verifica texto saveMessage()
 *		+ Chama pagina
 *		+ Verifica Valor do Registro[modifyRegisterNumber()]
 *	-Apagar
 *		+ Chama pagina
 *		+ Marca para deleção o registro adicionado
 *		+ Clica Botão Excluir
 *		+ Verificar texto deleteMessage()
 *
 * @author Lucas Gonçalves
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Tabular {
	Class<? extends ArangiTabularPage<? extends GenericLine>> page();
	Field[] fields();

	/**
	 * Message expected when a registry is saved successfully.
	 */
	String saveMessage() default "Dados gravados com sucesso!";

	/**
	 * By default it will execute the modifyRegistry. If it's not possible modify registry, set it to false.
	 */
	boolean modifyRegistry() default true;

	/**
	 * Message expected when a registry is modified successfully.
	 */
	String modifyMessage() default "Dados gravados com sucesso!";

	/**
	 * Message expected when a registry is deleted successfully.
	 */
	String deleteMessage() default "Dados excluídos com sucesso!";

	/**
	 * Message expected when a registry can not be deleted because there is
	 * one or more registries that depends on it.
	 */
	String dependencyMessage() default "";

	/**
	 *  Number of a line that contains a registry which can not be deleted because
	 *  there is one or more registries that depends on it. It is used to verify
	 *  if it is possible to delete a registry with dependencies.
	 */
	int dependencyLineNumber() default 0;

	/**
	 * Number of a line that contains the registry that will be modified in tests.
	 */
	int modifyRegisterNumber() default 0;

	/**
	 * Indicates if a new line is added on the top or at the botton of all lines on page
	 * when the button New is clicked.
	 */
	boolean addFirst() default true;

	/**
	 * Indicates if each line of the current tabular page has the buttons save and modify.
	 * If there are these buttons, it is not possible to edit all registry and save at once.
	 * So, editAll is FALSE. Otherwise, editAll has to be setted as TRUE.
	 */
	boolean editAll();

	/**
	 * Message expected when a registry is not saved because another registry with the same
	 * values has alredy been saved.
	 */
	String duplicityMessage() default "";

	/**
	 * Indicates the ID of the new registry added by the test case. In some pages aragi
	 * sorts all registry after save them, and in others the new registry is placed at
	 * the last line. When all registries are sorted, the value of "indexOrderedAfterSave"
	 * has to be the line number of the new registry after saved. Otherwise it has to be
	 * -1. If the value is -1, arangiTester identify the last line and verify if its
	 * value is the same as the new registry just added.
	 */
	int indexOrderedAfterSave() default -1;
}
