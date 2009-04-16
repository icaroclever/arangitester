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
package br.ufmg.lcc.arangitester.annotations;

import br.ufmg.lcc.arangitester.pages.ArangiSearchPage;
import br.ufmg.lcc.arangitester.pages.NullSearchPage;

/**
 * Configure behavior of a popup.
 * @author Lucas Gonçalves
 *
 */
public @interface PopUp {
	/**
	 * Page of the popup. Always a search page.
	 */
	Class<? extends ArangiSearchPage> searchPage() default NullSearchPage.class;
	
	/**
	 * Fields(search arguments) to be filled before the search. 
	 */
	PopField[] popFields() default {};
}
