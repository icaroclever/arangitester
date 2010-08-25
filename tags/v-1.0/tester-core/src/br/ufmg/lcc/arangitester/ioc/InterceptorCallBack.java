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
package br.ufmg.lcc.arangitester.ioc;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.VerifyAjax;
import br.ufmg.lcc.arangitester.interceptors.AjaxVerifcationImpl;
import br.ufmg.lcc.arangitester.interceptors.IInterceptor;
import br.ufmg.lcc.arangitester.interceptors.LoggerImpl;

/**
 * Class that decide if a interceptor will be call on invoked method. 
 * Only loggerInterceptor exist at this moment.
 * 
 * @author Lucas Gonçalves
 *
 */
public class InterceptorCallBack implements MethodInterceptor{
	private static IInterceptor loggerInterceptor = new LoggerImpl();
	private static IInterceptor ajaxInterceptor = new AjaxVerifcationImpl();
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Logger loggerConfig = method.getAnnotation(Logger.class);
		VerifyAjax ajaxAnnotation = method.getAnnotation(VerifyAjax.class);

		if ( loggerConfig != null ) loggerInterceptor.before(method, args, loggerConfig,obj);		
		if ( ajaxAnnotation != null ) ajaxInterceptor.before(method, args, ajaxAnnotation, obj);
		try{
			Object returnValue = proxy.invokeSuper(obj, args);
			
			if ( loggerConfig != null ) loggerInterceptor.after(method, args, loggerConfig,obj);
			
			return returnValue;
		}catch (Throwable e){
			loggerInterceptor.afterThrowing(method, args, e, loggerConfig,obj);
			throw e;

		}finally{
			if ( loggerConfig != null ) loggerInterceptor.afterFinally(method, args, loggerConfig,obj);
		}
	}
	
}
