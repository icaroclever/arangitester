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
package br.ufmg.lcc.arangitester.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Action when a custom annotation is on a method invoked.
 * To register annotation with the interceptor user registerInterceptor on LccContext
 * 
 * @author Lucas Gonçalves
 * 
 */
public interface IInterceptor {
	/**
	 * Called before execution of method
	 */
	void before(Method method, Object[] args, Annotation annotation, Object obj);

	/**
	 * Called after execution of method
	 */
	void after(Method method, Object[] args, Annotation annotation, Object obj);

	/**
	 * Called if method throw exception
	 */
	void afterThrowing(Method method, Object[] args, Throwable throwable, Annotation annotation, Object obj);

	/**
	 * Called on finally execution of method
	 */
	void afterFinally(Method method, Object[] args, Annotation annotation, Object obj);
}