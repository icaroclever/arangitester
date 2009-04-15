package br.ufmg.lcc.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.ufmg.lcc.arangitester.ui.IRequest;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestConfig {
	/**
	 * Forcewait in miliseconds 
	 */
	int forceWait() default 0;
	
	/**
	 * After request, new window may popup or close the popup window. 
	 * 
	 */
	IRequest.Window window() default IRequest.Window.CONTINUE;
	
	/**
	 * Some time, before a request, the application ask for a confirmation.
	 * 
	 */
	IRequest.Confirmation confirmation() default IRequest.Confirmation.NONE;
	
	/**
	 * If the request make a full submit selenium will wait for the new page to load.
	 * 
	 */
	boolean submit() default true;
	
	/**
	 * If the request use ajax it won't make a submit. If set to true on this property,
	 * it won't wait for page load(Selenium don't know that page was submitted)
	 * 
	 */
	boolean ajax() default false;
	
	/**
	 * After make the request, verify if a alert dialog exist. It will verify the text of alert dialog with this
	 * property. 
	 */
	String alert() default "";
}
