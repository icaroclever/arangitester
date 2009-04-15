package br.ufmg.lcc.arangitester.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controla o login. Se colocar sem definir o usuario então será usado o usuário 'admin', MAS se não for definido nenhum usuário e no arquivo tester-config.xml for definida o usuário na tag
 * default-login-username então este será usado. O mesmo acontece com a senha.
 * 
 * @author Lucas Gonçalves
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface Login {
	String user() default "NULL";

	String password() default "NULL";
}
