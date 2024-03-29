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

import org.arangitester.Context;
import org.arangitester.annotations.Field;
import org.arangitester.annotations.Login;
import org.arangitester.config.ConfigFactory;
import org.arangitester.exceptions.FatalException;
import org.arangitester.ui.UiPage;
import org.arangitester.util.ComponentElUtil;


/**
 * @author Lucas Gonçalves
 * 
 */
public abstract class BaseLoginController implements ILoginController {
	private boolean	alreadyLogged	= false;
	private String	user;

	protected abstract void login(String user, String password, Field[] fields) throws FatalException;

	/**
	 * Fill field on login page with values come from @Login.
	 * 
	 * @param fields
	 *            Extra Fields to be filled.
	 * @param pageLogin
	 *            Login page with the fields.
	 */
	protected void fillExtraFields(Field[] fields, UiPage pageLogin) {
		for (Field field : fields) {
			ComponentElUtil.fill(field.name(), field.value(), pageLogin);
		}
	}

	public void loginIfNeed(Object target, Method method) throws FatalException {
		Login login = target.getClass().getAnnotation(Login.class);
		if (method != null && method.getAnnotation(Login.class) != null) login = method.getAnnotation(Login.class);

		if (needChangeUser(login) || needLogOff(login)) {
			forceLogOff();
		}

		if (login != null && isAlreadyLogged() == false) {
			login(getUserName(login), getPassword(login), login.fields());
			user = getUserName(login);
			setAlreadyLogged(true);
		}
	}

	public void forceLogOff() throws FatalException {
		try {
			Context.getInstance().getSeleniumController().getSeleniumClient().deleteAllVisibleCookies();
		} catch (Throwable e) {
			Context.getInstance().getResult().addError("Nao foi possivel deletar os cookies", e);
			Context.getInstance().getSeleniumController().killClient();
		} finally {
			setAlreadyLogged(false);
			user = null;
		}
	}

	public boolean isAlreadyLogged() {
		return alreadyLogged;
	}

	public void setAlreadyLogged(boolean alreadyLogged) {
		this.alreadyLogged = alreadyLogged;
	}

	private boolean needLogOff(Login login) {
		return alreadyLogged && login == null;
	}

	private boolean needChangeUser(Login login) {
		return login != null && alreadyLogged && !user.equals(login.user());
	}

	private String getUserName(Login login) {
		if (login.user().equals("NULL")) {
			if (ConfigFactory.getConfig().getDefaultLoginUsername() != null) {
				return ConfigFactory.getConfig().getDefaultLoginUsername();
			} else {
				return "admin";
			}
		}
		return login.user();
	}

	private String getPassword(Login login) {
		if (login.password().equals("NULL")) {
			if (ConfigFactory.getConfig().getDefaultLoginPassword() != null) {
				return ConfigFactory.getConfig().getDefaultLoginPassword();
			} else {
				return "senha";
			}
		}
		return login.password();
	}
}
