package br.ufmg.lcc.arangitester.annotations;

/**
 * @author Lucas Gonçalves
 *
 */
public @interface PopField {
	String name();
	String cancelValue() default "";
	String modifyValue() default "";
	String addValue() default "";
}
