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
package br.ufmg.lcc.arangitester.util;

import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import br.ufmg.lcc.arangitester.el.CompositeResolver;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * Helper to Resolve El Expression. After create a instance of ElHelper, put
 * some variable on scope of El with method addVariable. After that will be
 * possible resolve expressions.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class ElHelper {
	private ExpressionFactory factory = new ExpressionFactoryImpl();
	private SimpleContext context = new SimpleContext(new CompositeResolver());

	public void addVariable(String name, Object value) {
		context.setVariable(name, factory.createValueExpression(value,
				Object.class));
	}

	public Object resolveElExpression(String exprStr) {
		ValueExpression expr = factory.createValueExpression(context, exprStr, Object.class);
		try {
			return expr.getValue(context);
		} catch (PropertyNotFoundException e) {
			return null;
		}
	}

}
