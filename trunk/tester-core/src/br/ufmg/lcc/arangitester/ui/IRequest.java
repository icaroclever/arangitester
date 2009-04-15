package br.ufmg.lcc.arangitester.ui;

import br.ufmg.lcc.arangitester.annotations.RequestConfig;

import com.thoughtworks.selenium.Selenium;

/**
 * Execute a action that trigger a request to server, and take care of common issues, like is ajax? or open new window?
 * 
 * Components that need do request to server must implement this interface.
 * To reconfigure this components use LccRequestConfig.
 * There is a basic implementation of it {@link Request}. It can be used by Composition.
 * 
 * @see RequestConfig for configurations.
 * @author Lucas Gonçalves
 *
 */
public interface IRequest {

	/**
	 * Execute a Action that to request to server. But take care of common
	 * problems on it, like control window and confirmation dialog.
	 * @param action command that trigger the request.
	 * @param sel Selenium instance.
	 */
	public void execute(IRequestCommand action, Selenium sel);
	
	/**
	 * Command interface. The action is a selenium call.
	 *
	 */
	interface IRequestCommand{
		void execute();
	}

	enum Window{
		OPEN, CLOSE, CONTINUE
	}

	enum Confirmation{
		OK, CANCEL, NONE
	}
}


