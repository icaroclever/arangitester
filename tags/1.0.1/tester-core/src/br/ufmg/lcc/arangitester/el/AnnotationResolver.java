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
package br.ufmg.lcc.arangitester.el;

import java.lang.annotation.Annotation;

import javax.el.BeanELResolver;
import javax.el.ELContext;

/**
 * Resolve methods for annotations.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class AnnotationResolver extends BeanELResolver {
	public Object getValue(ELContext context, Object base, Object property) {
		if (context == null) {
			throw new NullPointerException();
		}

		if (base == null || property == null || !(base instanceof Annotation)) {
			return null;
		}

		Object value = null;
		try {
			value = base.getClass().getMethod((String) property).invoke(base);
		} catch (Throwable e) {
			return null;
		}

		context.setPropertyResolved(true);

		return value;
	}

}
