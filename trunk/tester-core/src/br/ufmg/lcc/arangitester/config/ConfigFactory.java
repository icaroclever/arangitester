package br.ufmg.lcc.arangitester.config;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.exceptions.EnvException;

/**
 * Decide witch file is to read the configurations.
 * This decision is based on variable Environment named CI.
 * It cache the configurations.
 * @author Lucas Gonçalves
 * 
 */
public class ConfigFactory {
	private static Logger LOG = Logger.getLogger(ConfigFactory.class);
	private static Config config;
	private static ConfigEnv configenv;
	
	public static Config getConfig(){
		if ( config == null ){
			File configFile = new File("tester-config.xml");
			LOG.debug("Carregando arquivo de configuração: " + configFile.getAbsolutePath());
			try {
				config = TesterConfigReader.read(configFile);
			} catch (FileNotFoundException e1) {
				LOG.error("Diretório do arquivo de configuração: " + configFile.getAbsolutePath());
				throw new EnvException("O arquivo de configuração não existe", e1);
			}
		}
		return config;
	}
	
	public static ConfigEnv getEnvSpecificConfig(){
		if ( configenv == null ){

			String env = System.getProperty("user.name");
			if ( StringUtils.isBlank(env) ){
				env = getConfig().getDefaultEnv(); 
			}

			LOG.info("Usando configurações para ambiente: " + env);

			for ( ConfigEnv e: getConfig().getEnvironments() ){
				if (e.getName().equals(env) ){
					configenv = e;
					break;
				}
			}
		}
		return configenv;
	}
}