package br.ufmg.lcc.arangitester.annotations;

public @interface Field {
	String name();
	String cancelValue() default "";
	String modifyValue() default "";
	String addValue() default "";
	PopUp popup() default @PopUp;
}
