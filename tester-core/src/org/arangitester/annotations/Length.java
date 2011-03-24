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
package org.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to test the lenght of a field. <br/>
 * <br/>
 * <ul>
 * <li>Max: The max lenght that the field should accept
 * <li>Msg: The message that should appear in case we fill the field with more characteres that is allowed and try to save. This param is obrigatory and cannot be blank, if it is not
 * informed an error excetion will be thrown.<br/>
 * <li>checkSize: Set this to true to check the size of the field. Checks the maxLenght of the field, ie, the maximun of characteres that the interface(the field) allow the user to
 * enter. The default is FALSE.
 * </ul>
 * 
 * *** Temporally, when checkSize is True, the Msg will not be needed, since the user
 * will not be able to send more characters than allowed. When we get to intercept
 * the request of the page in tests and send more characteres than the field allows
 * us to do, the message will then be mandatory again, and the tests will check
 * if the validation is being done in the aplicaton too, and not just only in the
 * interface with the user(the maxLenght of the field).
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {
	int max();

	String msg();

	boolean checkSize() default false;
}
