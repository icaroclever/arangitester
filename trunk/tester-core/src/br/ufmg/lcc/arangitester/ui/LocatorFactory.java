package br.ufmg.lcc.arangitester.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.boot.LoginControllerFactory;
import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.exceptions.EnvException;

/**
 * Retorna uma instância de um LccIComponentLocator lendo o arquivo tester-config.xml. 
 * Se nenhum locator for configurado no arquivo tester-config.xml utilizará o padrão que é LccArangiComponentLocator
 * 
 * @author Lucas Gonçalves
 * 
 */
public class LocatorFactory {

	private static Logger LOG = Logger.getLogger(LoginControllerFactory.class);

	public static IComponentLocator getLocator() {
		String loginController = ConfigFactory.getConfig().getLocatorClass();
		if (StringUtils.isEmpty(loginController)) {
			loginController = "br.ufmg.lcc.arangitester.ArangiComponentLocator";
		}
		
		LOG.debug("Carregando Login Controller: " + loginController);
		try {
			Class<?> clazz = Class.forName(loginController);
			return (IComponentLocator) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new EnvException("Classe Locator não existe: " + loginController);
		} catch (Exception e) {
			throw new EnvException("Error ao carregar a classe Locator: " + loginController);
		}
		
	}
}
