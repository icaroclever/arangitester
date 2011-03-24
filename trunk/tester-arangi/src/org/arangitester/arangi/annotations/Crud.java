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
package org.arangitester.arangi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.arangitester.arangi.pages.ArangiPage;
import org.arangitester.arangi.pages.ArangiSearchPage;
import org.arangitester.arangi.pages.NullSearchPage;
import org.arangitester.arangi.pattern.CrudPattern;


/**
 * Configuration to use LccCrudPattern. It must be used on subclass of {@link CrudPattern}
 * @author Lucas Gonçalves
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Crud {
	/**
	 * Maintenance page.
	 */
	Class<? extends ArangiPage> page();
	Class<? extends ArangiSearchPage> searchPage() default NullSearchPage.class;
	
	/**
	 * Error message showed after save a registry without fill any field.
	 * Not verify if not setted.
	 */
	String messageRequiredFields() default "";
	
	/**
	 * Message showed after save a registry successfully. Default is 'Dados gravados com sucesso!' 
	 */
	String saveMessage() default "Dados gravados com sucesso!";
	
	/**
	 * Message showed after delete a registry successfully. Default is 'Dados excluídos com sucesso!' 
	 */
	String deleteMessage() default "Dados excluídos com sucesso!";
	
	/**
	 * Message showed after save duplicated registry.
	 * Not verify if not setted 
	 */
	String duplicityMessage() default "";
	
	/**
	 * Message showed after try delete registry with dependencies
	 * Not verify if not setted 
	 */
	String dependencyMessage() default "";
	
	/**
	 * Id (database) of registry with dependencies to be deleted.	
	 */
	int dependencyId() default 0;

	/**
	 * Fields values used on many tests.
	 */
	Field[] fields() default {};

	/**
	 * After add a new registry the search page is open and click on view of recentily added registry
	 * to verify.
	 * If set to false here, it will click on modify registry on search page otherwise in View registry.
	 * 
	 */
	boolean useViewToVerifyAddedRegistry() default true;
	
	/**
	 * DataBase Id of registry invoked to modify.
	 */
	int modifyId() default -1000;
	int searchLine() default 0;
	
	String searchLineValue() default "";
}
