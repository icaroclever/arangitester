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

import javax.el.ELContext;
import javax.el.ListELResolver;

import br.ufmg.lcc.arangitester.ui.UiSimpleLine;
import br.ufmg.lcc.arangitester.ui.UiTable;

/**
 * 
 * @author Lucas Gonçalves
 * 
 */
public class ListElResolver extends ListELResolver {

	@SuppressWarnings("unchecked")
	public Object getValue(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException();
		}

		if (base != null && base instanceof UiTable) {
			context.setPropertyResolved(true);
			UiTable<? extends UiSimpleLine> list = (UiTable<? extends UiSimpleLine>) base;
			int index = toInteger(property);
			return list.getLine(index);
		}
		return null;
	}

	private int toInteger(Object p) {
		if (p instanceof Integer) {
			return ((Integer) p).intValue();
		}
		if (p instanceof Character) {
			return ((Character) p).charValue();
		}
		if (p instanceof Boolean) {
			return ((Boolean) p).booleanValue() ? 1 : 0;
		}
		if (p instanceof Number) {
			return ((Number) p).intValue();
		}
		if (p instanceof String) {
			return Integer.parseInt((String) p);
		}
		throw new IllegalArgumentException();
	}
}
