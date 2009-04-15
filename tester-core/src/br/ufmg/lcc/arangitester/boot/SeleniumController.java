package br.ufmg.lcc.arangitester.boot;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.config.ConfigEnv;
import br.ufmg.lcc.arangitester.exceptions.EnvException;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

/**
 * Control server and client components of Selenium
 * @author Lucas Gonçalves
 *
 */
public class SeleniumController {

	private SeleniumServer seleniumServer;
	private Selenium seleniumClient;
	private ConfigEnv config;
	
	public SeleniumController(ConfigEnv config){
		this.config = config;
	}
	
	/**
	 * Destroys client clicking in close button
	 */
	public void destroyClient(){
		if ( seleniumClient != null ){
			try{
				seleniumClient.close();
			}catch (Exception e) {
				Context.getInstance().getResult().addError("O browser não foi inicializado, portanto não " +
						"há necessidade de clicar no botão fechar.");
			}
			seleniumClient.stop();
			seleniumClient = null;
		}
	}
	
	/**
	 * Destroys client ending the test session, killing the browser
	 */
	public void killClient(){
		if ( seleniumClient != null ){
			seleniumClient.stop();
			seleniumClient = null;
		}
	}

	public Selenium getSeleniumClient() {
		if ( seleniumClient == null ){
			
			//log.debug("Starting selenium client");
			//log.debug(" browser: " +config.getBrowser() + " url:" + getUrl());
			seleniumClient = new DefaultSelenium("localhost", getServer().getPort(), config.getBrowser(), getUrl());
			seleniumClient.start();
		try{
			seleniumClient.getBodyText();
		} catch (Exception e) {
				if(e.getMessage().contains("sessionId should not be null"))
					throw new EnvException("Não foi possível inicializar o selenium." +
						" Verifique as configurações do mesmo. Browser, servidor, porta, etc.",e);
			}
		}
		return seleniumClient;
	}
	
	private SeleniumServer getServer(){
		if ( seleniumServer == null ){
			try{
				//log.debug("Starting selenium server");
				RemoteControlConfiguration remoteControlConfiguration = new RemoteControlConfiguration();
				remoteControlConfiguration.setDontTouchLogging(true);
				remoteControlConfiguration.setMultiWindow(false);
				remoteControlConfiguration.setReuseBrowserSessions(false);
				seleniumServer = new SeleniumServer(remoteControlConfiguration);
				seleniumServer.start();
			} catch (Exception e) {
				throw new EnvException("It's not possible to start selenium server!", e);
			}
		}
		return seleniumServer;
	}
	
	private String getUrl(){
		return "http://" + config.getHost() + ":" + config.getPort();
	}

	/**
	 * Stop Selenium Server end clients
	 */
	public void stop(){
		if ( seleniumClient != null )
			destroyClient();
		if ( seleniumServer != null)
			seleniumServer.stop();
	}
}
