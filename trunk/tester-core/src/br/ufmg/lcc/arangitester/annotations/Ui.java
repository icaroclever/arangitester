package br.ufmg.lcc.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Ui{

	/**
	 * Id of element. If it has setted locator() will be discarted.
	 * On tables with name pattern registerForm:TABLENAME:INDEX:FIELDNAME
	 * define id on table object, and on element (inside table) put id=FIELDNAME.
	 */
	String id() default "";
	
	String desc() default "";

	String locator() default "";
	
	String name() default "";
}
