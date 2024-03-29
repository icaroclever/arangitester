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
 * Info for validate date field.
 * This field can be a date in format dd/mm/yyyy or can be a date with a specific hour like dd/mm/yyyy hh:mm
 * The 'min' and 'max' are long fields, so the date/date-hour field must be declared without '/' and ':' character
 * 
 * For example, 04/12/2008 must be declared like 4122008.
 * Another example, 04/12/2008 12:00 must be declared like 41220081200L
 * We must not include de first character if it is a number zero, because it will convert the date to an octal format.
 * 
 * If min() == max() it won't validate limits.
 * 
 * @author lukasmeirelles
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Date {
	long min() default 0;

	long max() default 0;

	String[] msg() default {};
}
