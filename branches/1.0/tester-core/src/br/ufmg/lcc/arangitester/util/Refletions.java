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

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import br.ufmg.lcc.arangitester.annotations.Test;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.ui.UiComponent;

public class Refletions {

	public static Object createTestClassInstance(Class<?> test) {
		try {
			return test.newInstance();
		} catch (Exception e) {
			throw new TesterException("Não foi possível instanciar a classe " + test.getName(), e);
		}
	}

	/**
	 * Get all the fields which are annotated with the given annotation. Returns an empty list
	 * if none are found.
	 */
	public static List<Field> getFields(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Field> fields = new ArrayList<Field>();
		List<String> fieldsName = new ArrayList<String>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			for (Field field : superClass.getDeclaredFields()) {
				if (field.isAnnotationPresent(annotation) && !fieldsName.contains(field.getName())) {
					fieldsName.add(field.getName());
					fields.add(field);
				}
			}
		}
		return fields;
	}

	/**
	 * Get all methods which are annotated with the given annotation. Methods on superclasses first!
	 * Returns an empty list if none are found.
	 */
	public static List<Method> getMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
		List<Method> methods = new ArrayList<Method>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			List<Method> tempMethods = new ArrayList<Method>();
			for (Method method : superClass.getDeclaredMethods()) {
				if (method.isAnnotationPresent(annotation)) {
					tempMethods.add(method);
				}
			}
			methods.addAll(0, tempMethods);
		}
		return methods;
	}

	/**
	 * Get all methods which are annotated with the given annotation. Methods on superclasses first!
	 * Returns an empty list if none are found.
	 */
	public static List<Method> getTestMethodsOrdered(Class<?> clazz) {
		List<Method> methods = new ArrayList<Method>();
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			List<Method> tempMethods = new ArrayList<Method>();
			Method[] declaredMethods = superClass.getDeclaredMethods();

			for (Method method : declaredMethods) {
				if (method.isAnnotationPresent(Test.class)) {
					tempMethods.add(method);
				}
			}
			Collections.sort(tempMethods, new Comparator<Method>() {
				@Override
				public int compare(Method m1, Method m2) {
					return ((Integer) m1.getAnnotation(Test.class).order()).compareTo((Integer) m2.getAnnotation(Test.class).order());
				}
			}
					);
			methods.addAll(0, tempMethods);
		}

		return methods;
	}

	public static Class<?> getGenericType(Object obj) {
		Type genericSuperclass = obj.getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			ParameterizedType parm = (ParameterizedType) genericSuperclass;
			return (Class<?>) parm.getActualTypeArguments()[0];
		}
		return null;
	}

	public static List<IUiComponent> getAllUiComponents(Class<? extends UiComponent> container, Object obj) {
		List<IUiComponent> components = new ArrayList<IUiComponent>();
		for (Field field : Refletions.getFields(container, Ui.class)) {
			try {
				components.add((UiComponent) Refletions.getFieldValue(field, obj));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return components;
	}

	public static List<IUiComponent> getAllUiComponents(IUiComponent obj) {
		List<IUiComponent> components = new ArrayList<IUiComponent>();
		for (Field field : Refletions.getFields(obj.getClass(), Ui.class)) {
			try {
				components.add((UiComponent) Refletions.getFieldValue(field, obj));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return components;
	}

	/**
	 * Get all components that is subclass of any components types.
	 * 
	 * @param container
	 *            Object that hold others components inside it.
	 * @param components
	 *            SuperTypes of components that will be returned.
	 * @return all components that is subclass of any component.
	 */
	
	@SuppressWarnings("unchecked")
	public static List<? extends IUiComponent> getAllUiComponents(IUiComponent container, @SuppressWarnings("rawtypes") Class... components) {
		List<IUiComponent> result = new ArrayList<IUiComponent>();

		for (IUiComponent component : Refletions.getAllUiComponents((Class<? extends UiComponent>) container.getClass(), (Object) container)) {
			for (Class<? extends IUiComponent> possibility : components) {
				if (possibility.isAssignableFrom(component.getClass().getSuperclass())) {
					result.add(component);
				}
			}
		}

		return result;
	}

	public static void setFieldValue(Field field, Object target, Object value) {
		try {
			if (Modifier.isPublic(field.getModifiers())) {
				field.set(target, value);
			} else {
				BeanUtils.setProperty(target, field.getName(), value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getFieldValue(String name, Object target) {
		Field field = null;
		try {
			field = getField(target.getClass(), name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getFieldValue(field, target);
	}

	public static Field getField(Class<?> clazz, String name) {
		for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(name);
			} catch (NoSuchFieldException nsfe) {}
		}
		return null;
	}

	public static Object getFieldValue(Field field, Object target) {
		Method getter = getGetterMethod(field.getDeclaringClass(), field.getName());
		try {
			if (getter != null) {
				return getter.invoke(target);
			} else {
				return field.get(target);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Method getGetterMethod(Class<?> clazz, String name) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.matches("^(get|is).*") && method.getParameterTypes().length == 0) {
				if (Introspector.decapitalize(methodName.substring(3)).equals(name)) {
					return method;
				}
			}
		}
		return null;
	}

}
