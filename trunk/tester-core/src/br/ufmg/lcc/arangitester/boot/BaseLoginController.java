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
package br.ufmg.lcc.arangitester.boot;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Field;
import br.ufmg.lcc.arangitester.annotations.Login;
import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.exceptions.FatalException;
import br.ufmg.lcc.arangitester.ui.UiPage;
import br.ufmg.lcc.arangitester.util.ComponentElUtil;

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

	@Override
	public void loginIfNeed(Object target, Method method) throws FatalException {
		Login login = target.getClass().getAnnotation(Login.class);

		Login methodLogin = null;
		Field[] extraFields = null;
		if (login != null) login.fields();

		if (method != null) {
			methodLogin = method.getAnnotation(Login.class);
		}

		String username = null;
		String password = null;

		if ((methodLogin != null && !user.equals(methodLogin.user()))) {
			forceLogOff();
			username = methodLogin.user();
			password = methodLogin.password();
			extraFields = methodLogin.fields();
		} else if (login != null) {
			if (login.user().equals("NULL")) {
				if (StringUtils.isNotBlank(ConfigFactory.getConfig().getDefaultLoginUsername())) {
					username = ConfigFactory.getConfig().getDefaultLoginUsername();
				} else {
					username = "admin";
				}
			} else {
				username = login.user();
			}
			if (login.password().equals("NULL")) {
				if (StringUtils.isNotBlank(ConfigFactory.getConfig().getDefaultLoginPassword())) {
					password = ConfigFactory.getConfig().getDefaultLoginPassword();
				} else {
					password = "senha";
				}
			} else {
				password = login.password();
			}
		}

		if (login != null && alreadyLogged == false) {
			login(username, password, extraFields);
			if (methodLogin != null)
				user = methodLogin.user();
			else
				user = login.user();
			alreadyLogged = true;
		}
	}

	@Override
	public void forceLogOff() throws FatalException {
		try {
			Context.getInstance().getSeleniumController().getSeleniumClient().deleteAllVisibleCookies();
		} catch (Throwable e) {
			Context.getInstance().getResult().addError("Nao foi possivel deletar os cookies", e);
			Context.getInstance().getSeleniumController().killClient();
		} finally {
			setAlreadyLogged(false);
		}
	}

	@Override
	public boolean isAlreadyLogged() {
		return alreadyLogged;
	}

	@Override
	public void setAlreadyLogged(boolean alreadyLogged) {
		this.alreadyLogged = alreadyLogged;
	}
}
