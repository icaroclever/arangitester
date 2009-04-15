package br.ufmg.lcc.arangitester.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Action when a custom annotation is on a method invoked.
 * To register annotation with the interceptor user registerInterceptor on LccContext
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