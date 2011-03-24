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

import org.arangitester.arangi.ui.Button;


/**
 * Common configuration for UiPage
 * 
 * @author Lucas Gonçalves
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Page{
	String url();
	
	/**
	 * The ARANGI edit page's title, in the state of EDITING(when you are altering
	 * some registry).<br/>
	 * <B>Just use this in case the page is a editPage, in the altering(editing) state. </B><br/>
	 * The title may follow the pattern "Alteração de xxxx".<br/> 
	 * Example: Alteração de Agendamento, Alteração de ETCs, etc
	 * <br/><br/>For more information, see Redmine's Feature #1079 - Corrigir títulos das telas do sistema
	 * @return
	 */
	String editTitle() default "";
	
	/**
	 * The ARANGI edit page's title, in the state of INCLUDING(when you are inserting
	 * a new registry)<br/>
	 * <B>Just use this in case the page is a editPage, in the including state. </B><br/>
	 * The title may follow the pattern "Inclusão de xxxx".<br/> 
	 * Example: Inclusão de Agendamento, Inclusão de ETCs, etc
	 * <br/><br/>For more information, see Redmine's Feature #1079 - Corrigir títulos das telas do sistema
	 * @return
	 */
	String includeTitle() default "";
	
	/**
	 * The ARANGI edit page's title, in the state of VIEWING(when you are just
	 * viewing a registry)<br/>
	 * <B>Just use this in case the page is an editPage, in the viewing state. </B><br/>
	 * The title may follow the pattern "Inclusão de xxxx".<br/> 
	 * Example: Visualização de Agendamento, Visualização de ETCs, etc
	 * <br/><br/>For more information, see Redmine's Feature #1079 - Corrigir títulos das telas do sistema
	 * @return
	 */
	String viewTitle() default "";
	
	/**
	 * The ARANGI search page's title. <br/>
	 * <B>Just use this in case the page is a Search page. </B><br/>
	 * Very often the title will look like this:
	 * "Pesquisa de xxx". Example: "Pesquisa de processamentos", "Pesquisa de ETCs", etc.
	 * <br/><br/>For more information, see Redmine's Feature #1079 - Corrigir títulos das telas do sistema
	 */
	String searchTitle() default "";
	
	
	/**
	 * The ARANGI editPage's subtitles, in order they appear on screen, top-down.<br/>
	 * <B>Just use this in case the page is an editPage. </B><br/>
	 * The subtitles will be free, and may vary
	 * depending of the page.
	 * Some examples are "Ítens do Processo" or "Atualização de Parãmetros", etc.
	 * <br/><br/>For more information, see Redmine's Feature #1079 - Corrigir títulos das telas do sistema
	 */
	String[] editSubTitles() default {};

	/**
	 * The ARANGI searchPage's subtitles, in order they appear on screen, top-down.<br/>
	 * <B>Just use this in case the page is a searchPage. </B><br/>
	 * By default, the first subtitle will be "Parâmetros para Pesquisa", and the
	 * second subtitle will be "Resultados da Pesquisa".<br/>
	 * Change it if you need.
	 * <br/><br/>For more information, see Redmine's Feature #1079 - Corrigir títulos das telas do sistema
	 */
	String[] searchSubTitles() default {"Parâmetros para Pesquisa", "Resultados da Pesquisa"};
	
	Button[] searchButtons() default {};
	Button[] editButtons() default {};
	Button[] viewButtons() default {};
	
}
