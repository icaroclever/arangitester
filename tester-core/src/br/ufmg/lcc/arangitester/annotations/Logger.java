package br.ufmg.lcc.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Send a message to logger. The message is formated like this:
 * 
 * MESSAGE          :OK on success
 * MESSAGE       :ERROR on failure
 * 
 * Error message is printed on exception throw.
 * @author Lucas Gonçalves
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Logger {
	/**
	 * Message to print.
	 */
	String value();
}
