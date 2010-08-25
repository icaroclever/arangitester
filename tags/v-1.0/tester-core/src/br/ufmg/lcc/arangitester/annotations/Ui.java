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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Ui{

	/**
	 * Id of element. If it has setted locator() will be discarted.
	 * On tables with name pattern registerForm:TABLENAME:INDEX:FIELDNAME
	 * define id on table object, and on element (inside table) put id=FIELDNAME.
	 */
	String id() default "";
	
	String desc() default "";

	String locator() default "";
	
	String name() default "";
	
	
	/**
	 * Index of element. If the id or name of the element in html page has not been informed,
	 * the index of the object page can be used. Eg.: /table[1] is the first table found in the 
	 * page. 
	 */
	String index() default "";
}
