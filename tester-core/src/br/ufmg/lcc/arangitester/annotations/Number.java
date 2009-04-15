package br.ufmg.lcc.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Info for validate number field.
 * If min() == max() it won't validate limits, but still verifing if it is a number.
 * 
 * @author Lucas Gonçalves
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Number {
	int min() default 0;
	int max() default 0;
	
	/**
	 * Message showing when a number is less than the min value allowed
	 */
	String msgMinValue();
	
	/**
	 * Message showing when a number is greater than the max value allowed
	 */
	String msgMaxValue();
	
	/**
	 * Message showing when the input is not a number
	 */
	String msgWrongValue();
}
