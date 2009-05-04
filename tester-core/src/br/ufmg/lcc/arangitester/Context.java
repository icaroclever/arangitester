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
package br.ufmg.lcc.arangitester;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.boot.SeleniumController;
import br.ufmg.lcc.arangitester.config.ConfigEnv;
import br.ufmg.lcc.arangitester.config.ConfigFactory;
import br.ufmg.lcc.arangitester.exceptions.LccException;
import br.ufmg.lcc.arangitester.log.IResult;
import br.ufmg.lcc.arangitester.log.Result;

/**
 * Context of tests. All mainly objects needed to execute a test will be access from here.
 * DP Singleton.
 * @author Lucas Gonçalves
 *
 */
public class Context {
	private static Context instance;
	
	private SeleniumController seleniumController;
	
	private ConfigEnv serverConfig;
	
	private static File tempDir = null;
	
	/**
	 * Result of executions.
	 */
	private IResult result = new Result();

	private Context() {
		serverConfig = ConfigFactory.getEnvSpecificConfig();
		seleniumController = new SeleniumController(serverConfig);
		System.out.println("Diretório temporário " + getTempDirectory().getAbsolutePath());
	}

	public static Context getInstance() {
		if (instance == null) {
			instance = new Context();
		}
		return instance;
	}
	
	
	public File getTempDirectory(){
		if(Context.tempDir==null){
		    
			File resultFile = new File("").getAbsoluteFile();
			Context.tempDir =  new File( resultFile.getParentFile(), "temp");
		}
		
		return Context.tempDir;
	}
	
	public File getResultDir() {
        String property = System.getProperty("result.out");
        if (StringUtils.isNotBlank(property)){
            return new File(property);
        }
        return Context.getInstance().getTempDirectory();
	}
	
	public File getResultFile() {
	    return new File(this.getResultDir(), "functionalTests.xml");
	}
	
	public File getScreenshotDir() {
	    return new File(this.getResultDir(), "htmls");
	}
	
	public File getScreenshotHtmlDir(){
		if (!this.getScreenshotDir().exists()) {
            if (!this.getScreenshotDir().mkdirs()) {
                throw new LccException("Erro ao tentar criar o diretorio " + this.getScreenshotDir().getAbsolutePath());
            }
        }
		
		return this.getScreenshotDir();
	}
	
	public File getScreenshotPngDir(){
	    return this.getScreenshotHtmlDir();
	}
	
	public File getScreenshotDefaultDir(){
	    return this.getScreenshotHtmlDir();
	}

	public IResult getResult(){
		return result;
	}

	public SeleniumController getSeleniumController(){
		return seleniumController;
	}
	
	public ConfigEnv getConfig() {
		return serverConfig;
	}
}
