package br.ufmg.lcc.arangitester.boot;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Login;
import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.exceptions.FatalException;

/**
 * @author Lucas Gonçalves
 *
 */
public abstract class BaseLoginController implements ILoginController {
	private boolean alreadyLogged = false;
	private String user;
	
	protected abstract void login(String user, String password) throws FatalException;

	@Override
	public void loginIfNeed(Object target, Method method) throws FatalException{
		Login login = target.getClass().getAnnotation(Login.class);
		
		Login methodLogin = null;
		
		if ( method != null ){
			methodLogin = method.getAnnotation(Login.class);	
		}
		
		String username = null;
		String password = null;
		
		if (( methodLogin != null && !user.equals(methodLogin.user()))){
			forceLogOff();
			username = methodLogin.user();
			password = methodLogin.password();
		} else if(login != null){
			if (login.user().equals("NULL")){
				if (StringUtils.isNotBlank(ConfigFactory.getConfig().getDefaultLoginUsername())){
					username = ConfigFactory.getConfig().getDefaultLoginUsername();
				} else {
					username = "admin";
				}
			} else {
				username = login.user();
			}
			if (login.password().equals("NULL")){
				if (StringUtils.isNotBlank(ConfigFactory.getConfig().getDefaultLoginPassword())){
					password = ConfigFactory.getConfig().getDefaultLoginPassword();
				} else {
					password = "senha";
				}
			} else {
				password = login.password();
			}
		}
		
		if ( login != null && alreadyLogged == false){
			login(username, password);
			user = login.user();
			alreadyLogged = true;
		}
	}

	@Override
	public void forceLogOff() throws FatalException{
		try{
			Context.getInstance().getSeleniumController().getSeleniumClient().deleteAllVisibleCookies();
		}catch (Throwable e){
			Context.getInstance().getResult().addError("Nao foi possivel deletar os cookies",e);
			Context.getInstance().getSeleniumController().killClient();
		}finally{
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
