package br.ufmg.lcc.arangitester.boot;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.exceptions.EnvException;

/**
 * Instaciate a loginController from config.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class LoginControllerFactory {
	private static Logger LOG = Logger.getLogger(LoginControllerFactory.class);

	public static ILoginController getLoginController() {
		String loginController = ConfigFactory.getConfig().getLoginController();
		if (StringUtils.isEmpty(loginController)) {
			loginController = "br.ufmg.lcc.arangitester.boot.ArangiLoginController";
		}

		LOG.debug("Carregando Login Controller: " + loginController);
		try {
			Class<?> clazz = Class.forName(loginController);
			return (ILoginController) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new EnvException("Classe de controle de login não existe: " + loginController);
		} catch (InstantiationException e) {
			throw new EnvException("Error ao carregar a classe de controle de login: " + loginController);
		} catch (IllegalAccessException e) {
			throw new EnvException("Error ao carregar a classe de controle de login: " + loginController);
		}

	}
}
