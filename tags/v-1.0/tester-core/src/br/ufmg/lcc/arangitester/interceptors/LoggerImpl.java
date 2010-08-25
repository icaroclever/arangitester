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
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.log.IResult;
import br.ufmg.lcc.arangitester.util.ElHelper;
import br.ufmg.lcc.arangitester.util.ArangiTesterStringUtils;

import com.thoughtworks.selenium.Wait.WaitTimedOutException;

/**
 * 
 * Interceptor associated with LccLogger
 * It only intercepts methods related to UiComponent created with UiComponentFactory.
 * @see UiComponentFactory
 * @author Lucas Gonçalves
 *
 */
public class LoggerImpl implements IInterceptor{
	public static final String STATUS_OK = ":OK";
	public static final String STATUS_ERROR = ":ERROR";
	private ElHelper el = new ElHelper();
	
	
	public void after(Method method, Object[] args, Annotation annotation, Object obj) {
		Logger conf = (Logger)annotation;
		String msg  = interpolate(conf.value(), args, obj);	
		getLogger().addInfo(getFormatedStatusMsg(msg, STATUS_OK));
	}

	public void afterFinally(Method method, Object[] args, Annotation annotation, Object obj) {

	}

	public void afterThrowing(Method method, Object[] args, Throwable throwable, Annotation annotation, Object obj) {
		Logger conf;
		String msg, screenShotName;
		
		if(annotation==null){
			msg = "Erro genérico.";
		}else{
			conf = (Logger)annotation;
			msg = interpolate(conf.value(), args, obj);
		}
		
		// Captura e salva um screenshot dos testes. Diretorios configurados no LccContext
		screenShotName = getLogger().getScreenshotName(new Date(), method);
		getLogger().takeScreenshot(screenShotName);
		 
		if ( throwable instanceof WaitTimedOutException){
			getLogger().addError(getFormatedStatusMsg(msg, STATUS_ERROR));
			getLogger().addError("Element '" + throwable.getMessage() + "' is not present!");
		}else{
			getLogger().addError(getFormatedStatusMsg(msg, STATUS_ERROR), throwable);
		}
		
		// Adiciona o Screenshot ao Log
		getLogger().addScreenShot(screenShotName + ".png", "error");
	}

	public void before(Method method, Object[] args, Annotation annotation, Object obj) {
	}

	public static String getFormatedStatusMsg(String msg, String status){
		int size = 100; 
		int spacesNeeded = size - status.length();
		if ( spacesNeeded > 0)
			msg = StringUtils.rightPad(msg, spacesNeeded);
		return msg + status;
	}
	
	private IResult getLogger(){
		return Context.getInstance().getResult();
	}
	
	private String interpolate(String text, Object[] args, Object from){
		fillElContext(args, from);
		String elResolv = (String) el.resolveElExpression(text);
		if ( elResolv != null ){
			text = elResolv;
		}
		String[] rep = new String[args.length];
		String[] argsStr = new String[args.length];
		for (int i=0; i < args.length; i++){
			argsStr[i] = args[i].toString();
			rep[i] = "#" + i;
		}
		
		text = ArangiTesterStringUtils.interpolate(text, from);
		return StringUtils.replaceEach(text, rep, argsStr);
	}

	private void fillElContext(Object[] args, Object obj){
		el.addVariable("this", obj);
		
		for ( int i=0; i< args.length; i++){
			el.addVariable("arg" + i, args[i]);
		}
	}
}
