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

/**
 * Controla o login. Se colocar sem definir o usuario então será usado o usuário
 * 'admin', MAS se não for definido nenhum usuário e no arquivo
 * tester-config.xml for definida o usuário na tag default-login-username então
 * este será usado. O mesmo acontece com a senha.
 * 
 * @author Lucas Gonçalves
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface Login {
	String user() default "NULL";

	String password() default "NULL";

	/**
	 * Login and password don´t go here otherwise use user and password
	 * atributes.
	 * 
	 * @return Extra fields that must be filled on login page.
	 */
	Field[] fields() default {};
}
