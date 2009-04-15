package br.ufmg.lcc.arangitester.ui;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.RequestConfigImp;
import br.ufmg.lcc.arangitester.ioc.ICreate;
import br.ufmg.lcc.arangitester.ui.IRequest.IRequestCommand;
/**
 * Permit a common action click().
 * @author Lucas Gonçalves
 *
 */
public class UiClickable extends UiComponent implements ICreate{
	private Request requestDelegate;
	
	public void clickWithOutLogger(){
		waitElement(getComponentLocator());
		
		IRequestCommand action = new IRequestCommand(){

			@Override
			public void execute() {
				getSel().click(getComponentLocator());
			}

		};
		requestDelegate.execute(action, getSel());
	}
	
	/**
	 * Click on button
	 */
	@Logger("Clicando: #{componentDesc}")
	public void click(){
		clickWithOutLogger();
	}

	public RequestConfigImp getRequestConfig() {
		return requestDelegate.getRequestConfig();
	}

	@Override
	public void create() {
		requestDelegate = new Request(getConfig(RequestConfig.class));
	}

}
