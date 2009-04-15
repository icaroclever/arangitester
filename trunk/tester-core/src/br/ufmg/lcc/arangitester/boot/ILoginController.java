package br.ufmg.lcc.arangitester.boot;

import java.lang.reflect.Method;

import br.ufmg.lcc.arangitester.exceptions.FatalException;

public interface ILoginController {

	/**
	 * Do login if it wasn't logged and the class has annotation for loggin
	 * @param method being executed.
	 * @throws FatalException 
	 */
	public abstract void loginIfNeed(Object target, Method method) throws FatalException;

	/**
	 * Try clearing all cookies. If there are any error clearing cookies, close browser.
	 * @throws FatalException 
	 */
	public abstract void forceLogOff() throws FatalException;

	public abstract boolean isAlreadyLogged();

	public abstract void setAlreadyLogged(boolean alreadyLogged);

}