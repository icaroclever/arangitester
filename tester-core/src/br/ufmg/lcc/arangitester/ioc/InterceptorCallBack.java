package br.ufmg.lcc.arangitester.ioc;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import br.ufmg.lcc.arangitester.annotations.Logger;
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
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Logger loggerConfig = method.getAnnotation(Logger.class);

		if ( loggerConfig != null ) loggerInterceptor.before(method, args, loggerConfig,obj);		
		
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
