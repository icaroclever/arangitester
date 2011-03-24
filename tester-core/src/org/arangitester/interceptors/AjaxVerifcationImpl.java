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
package org.arangitester.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.arangitester.Context;
import org.arangitester.annotations.VerifyAjax;


/**
 * Intercept method with {@link VerifyAjax} annotation.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class AjaxVerifcationImpl implements IInterceptor {

	@Override
	public void after(Method method, Object[] args, Annotation annotation, Object obj) {

	}

	@Override
	public void afterFinally(Method method, Object[] args, Annotation annotation, Object obj) {}

	@Override
	public void afterThrowing(Method method, Object[] args, Throwable throwable, Annotation annotation, Object obj) {}

	@Override
	public void before(Method method, Object[] args, Annotation annotation, Object obj) {
		if (Context.getInstance().isVerifyAjaxRequest()) {
			Context.getInstance().getSeleniumController().getSeleniumClient().waitForCondition("Ajax.activeRequestCount == 0", "10000");
		}
	}

}
