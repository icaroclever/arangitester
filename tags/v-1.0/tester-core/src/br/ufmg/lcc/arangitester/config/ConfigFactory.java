/*
 * Copyright 2000 Universidade Federal de Minas Gerais.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.ufmg.lcc.arangitester.config;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.exceptions.EnvException;

/**
 * Decide witch file is to read the configurations. This decision is based on variable Environment named CI.
 * It cache the configurations.
 * @author Lucas Gonçalves
 */
public class ConfigFactory {
    private static Logger LOG = Logger.getLogger(ConfigFactory.class);
    private static Config config;
    private static ConfigEnv configenv;

    public static Config getConfig() {
        if (config == null) {
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

    public static ConfigEnv getEnvSpecificConfig() {
        if (configenv == null) {
            String env = System.getProperty("user.name");
            env = StringUtils.replace(env, "‡", "ç");
            configenv = ConfigFactory.getEnv(env);

            if (configenv == null && StringUtils.isNotBlank(getConfig().getDefaultEnv())) {
                configenv = ConfigFactory.getEnv(getConfig().getDefaultEnv());
            }

            LOG.info("Usando configurações para ambiente: " + env);

        }
        return configenv;
    }

    private static ConfigEnv getEnv(String env) {
        for (ConfigEnv e : getConfig().getEnvironments()) {
            if (e.getName().equals(env)) {
                return e;
            }
        }
        return null;
    }
}
