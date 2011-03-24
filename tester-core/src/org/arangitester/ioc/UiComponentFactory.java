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
package org.arangitester.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import net.sf.cglib.proxy.Enhancer;

import org.apache.log4j.Logger;
import org.arangitester.annotations.Ui;
import org.arangitester.exceptions.TesterException;
import org.arangitester.ui.IUiComponent;
import org.arangitester.ui.IUiTable;
import org.arangitester.ui.UiTable;
import org.arangitester.util.Refletions;


/**
 * Create proxy class and set all annotations to it
 * 
 * @author Lucas Gonçalves
 * 
 */
public class UiComponentFactory {

	private static Logger	LOG	= Logger.getLogger(UiComponentFactory.class);

	/**
	 * Create a new proxy class.
	 */
	public static <T> T getInstance(Class<T> concrete) {
		return getInstance(concrete, null);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getInstance(Class<T> concrete, Annotation[] configs) {

		T newProxy = createProxyClass(concrete);
		if (configs != null) {
			((IUiComponent) newProxy).setConfigs(configs);
		}

		for (Field field : Refletions.getFields(concrete, Ui.class)) {
			LOG.debug("Criando child UiComponent: " + field.getName());
			IUiComponent newInstanceDep = (IUiComponent) UiComponentFactory.getInstance(field.getType(), field.getAnnotations());
			if (field.getGenericType() instanceof ParameterizedType) {
				if (field.getType().isAssignableFrom(UiTable.class)) {
					IUiTable<?> table = (IUiTable<?>) newInstanceDep;
					ParameterizedType pType = (ParameterizedType) field.getGenericType();
					@SuppressWarnings("rawtypes")
					Class t = (Class<?>) pType.getActualTypeArguments()[0];
					table.setType(t);
				}
			}
			Refletions.setFieldValue(field, newProxy, newInstanceDep);
		}

		if (newProxy instanceof ICreate) {
			ICreate create = (ICreate) newProxy;
			create.create();
		}
		return newProxy;
	}

	@SuppressWarnings("unchecked")
	private static <T> T createProxyClass(Class<T> concrete) {
		try {
			concrete.getConstructor();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TesterException("A classe " + concrete.getSimpleName() + " não tem um construtor sem parametros" +
					"\n" + e.toString() + "\n" + e.getStackTrace().toString());
		}
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(concrete);
		enhancer.setCallback(new InterceptorCallBack());

		T proxy = (T) enhancer.create();

		return proxy;
	}

}
