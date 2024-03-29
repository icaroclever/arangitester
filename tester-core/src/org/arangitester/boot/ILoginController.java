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
package org.arangitester.boot;

import java.lang.reflect.Method;

import org.arangitester.exceptions.FatalException;


public interface ILoginController {

	/**
	 * Do login if it wasn't logged and the class has annotation for loggin
	 * 
	 * @param method
	 *            being executed.
	 * @throws FatalException
	 */
	public abstract void loginIfNeed(Object target, Method method) throws FatalException;

	/**
	 * Try clearing all cookies. If there are any error clearing cookies, close browser.
	 * 
	 * @throws FatalException
	 */
	public abstract void forceLogOff() throws FatalException;

	public abstract boolean isAlreadyLogged();

	public abstract void setAlreadyLogged(boolean alreadyLogged);

}