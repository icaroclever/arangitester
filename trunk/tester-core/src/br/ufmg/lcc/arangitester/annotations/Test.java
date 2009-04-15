package br.ufmg.lcc.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define um método de teste.
 * 
 * @author Lucas Gonçalves
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
	/** Título do teste. */
	String value();

	/**
	 * Nome do método (s) que este teste é dependente. Caso o método dependente
	 * ocorra erro, este não será executado e será marcado como skiped.
	 */
	String dependency() default "";

	/** Ordem que o teste será executado dentro do testcase. */
	int order();
}
