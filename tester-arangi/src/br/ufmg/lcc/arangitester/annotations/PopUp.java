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
