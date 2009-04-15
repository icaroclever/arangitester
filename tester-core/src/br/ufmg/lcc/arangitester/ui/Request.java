package br.ufmg.lcc.arangitester.ui;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.RequestConfigImp;
import br.ufmg.lcc.arangitester.exceptions.LccException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.interceptors.LoggerImpl;
import br.ufmg.lcc.arangitester.util.LccStringUtils;
import br.ufmg.lcc.arangitester.util.SeleniumScripts;

import com.thoughtworks.selenium.Selenium;


/**
 * It can be used for components that need do request to server.
 * 
 * @author Lucas Gonçalves
 *
 */
public class Request implements IRequest{
	private Logger LOG = Logger.getLogger(Request.class);
	private RequestConfigImp config;
	
	public Request(RequestConfig config){
		this.config = new RequestConfigImp(config);
	}
	
	public void execute(IRequestCommand action, Selenium sel) {
		if ( getRequestConfig().getConfirmation() == IRequest.Confirmation.CANCEL ){
			LOG.debug("Choose cancel on next Confirmation");
			sel.chooseCancelOnNextConfirmation();
		} else if ( getRequestConfig().getConfirmation() == IRequest.Confirmation.OK ){
			LOG.debug("Choose Ok on next Confirmation");
			sel.chooseOkOnNextConfirmation();
		}
		
		action.execute();
		
		if ( StringUtils.isNotBlank(getRequestConfig().getAlert()) ){
			String alertCurrent = sel.getAlert();
			if ( !LccStringUtils.containsWithoutSpaces(getRequestConfig().getAlert(), alertCurrent) ){
				throw new LccException("Mensagem de alerta esperada: " + getRequestConfig().getAlert() + " \nmas a atual é: " + alertCurrent);
			}
			LOG.info(LoggerImpl.getFormatedStatusMsg("Verificando alerta: " + getRequestConfig().getAlert(), "OK"));
		}
		
		if ( getRequestConfig().getConfirmation() == IRequest.Confirmation.CANCEL || getRequestConfig().getConfirmation() == IRequest.Confirmation.OK){
			sel.getConfirmation();
		} 
		
		if ( getRequestConfig().isSubmit() && !getRequestConfig().isAjax() && getRequestConfig().getWindow() == IRequest.Window.CONTINUE){
			try{
				LOG.debug("Waiting for new page load");
				sel.waitForPageToLoad(UiPage.DEFAULT_PAGE_WAIT_TIME);
			}catch (Throwable e) {
				throw new TesterException("Time out ao espera a página. Talvez a página não tenha sido submetida. " +
						"Ela pode ter feito apenas uma chama utilizando ajax.");
			}
			
		}
		
		if ( getRequestConfig().getWindow() == IRequest.Window.OPEN ){
			LOG.debug("Opening new window");
			SeleniumScripts.trocarNomeDeJanelaVazia(sel);
			String[] allWindowNames = sel.getAllWindowNames();
			String lastWindow = allWindowNames[allWindowNames.length -1];
			sel.waitForPopUp(lastWindow, "10000");
			sel.selectWindow(lastWindow);
		} else if ( getRequestConfig().getWindow() == IRequest.Window.CLOSE ){
			LOG.debug("Closing new window");
			String[] allWindowNames = sel.getAllWindowNames();
			String lastWindow = allWindowNames[allWindowNames.length -1];
			sel.selectWindow(lastWindow);
		}
		
		if ( getRequestConfig().getForceWait() != 0 ){
			try {
				LOG.debug("Wainting forced");
				Thread.sleep(getRequestConfig().getForceWait());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}

	public RequestConfigImp getRequestConfig() {
		if ( config == null ){
			config = new RequestConfigImp();
		}
		return config;
	}
}
