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
package org.arangitester.el;

import javax.el.BeanELResolver;
import javax.el.ELContext;

import org.arangitester.util.Refletions;


public class BeanResolver extends BeanELResolver {
	public Object getValue(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException();
		}

		if (base == null || property == null) {
			return null;
		}

		Object value = null;
		try {
			value = Refletions.getFieldValue((String) property, base);
		} catch (Throwable e) {
			return null;
		}

		context.setPropertyResolved(true);

		return value;
	}
}