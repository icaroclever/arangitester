package br.ufmg.lcc.arangitester.ui;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.RequestConfig;
import br.ufmg.lcc.arangitester.annotations.RequestConfigImp;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.LccException;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ioc.ICreate;
import br.ufmg.lcc.arangitester.ui.IRequest.IRequestCommand;

import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

/**
 * Represent a simple select tag on HTML. This component can do request to server.
 * 
 * @see RequestConfig
 * @author Lucas Gonçalves
 *
 */
public class UiSelect extends UiComponent implements ICreate{
	private Request requestDelegate;

	
	@Override
	public void create() {
		requestDelegate = new Request(super.getConfig(RequestConfig.class));
		requestDelegate.getRequestConfig().setSubmit(false);
	}
	
	/**
	 * Select a option in select tag.
	 * @param label caption of option
	 */
	@Logger("Selecionando opção em #{componentDesc}: '#0'")
	public void select(final String label){
		waitElement(getComponentLocator());
		waitOption(getComponentLocator(), label);
		
		if ( !getSel().isSomethingSelected(getComponentLocator()) || !(label.equals(getSel().getSelectedLabel(getComponentLocator()))) ){

			IRequestCommand action = new IRequestCommand(){

				@Override
				public void execute() {
					getSel().select(getComponentLocator(), "label=" + label + "");	
				}

			};
		
			requestDelegate.execute(action, getSel());
			
			// Testa pós-condição
			if(!label.equals(getSelectedLabel())){
				throw new LccException("Erro ao selecionar elemento. Opção '"+
						label +"' não foi selecionada corretamente.");
			}
			
			setPreviewslyActionValue(label);
		}	

	}

	@Logger("Verificando '#{componentDesc}': '#0'")
	public void verifySelect(String label){
		waitElement(getComponentLocator());
		try{
			waitOption(getComponentLocator(), label);
		}catch (ElementNotExistException e) {
			throw new WrongValueException("A opção "+ label + "não existe no select.");
		}
		String selectedLabel = getSel().getSelectedLabel(getComponentLocator());
		if (!selectedLabel.equals(label)){
			throw new WrongValueException("Expected option: " + label +  
					" but actually is: '" + selectedLabel + "'");
		}
	}
	
	public String getSelectedLabel(){
		waitElement(getComponentLocator());
		
		try{
			new Wait(){
				@Override
				public boolean until() {
					return getSel().getSelectedLabel(getComponentLocator()) != null;
				}
			}.wait(getComponentLocator(), DEFAULT_ELEMENT_WAIT_TIME);
		}catch(WaitTimedOutException e){
			return null;
		}
		return getSel().getSelectedLabel(getComponentLocator());
	}
	
	private void waitOption(final String locator, final String label){
		try{
			new Wait(){
				@Override
				public boolean until() {
					return ArrayUtils.contains(getSel().getSelectOptions(locator), label);
				}
			}.wait(locator, DEFAULT_ELEMENT_WAIT_TIME);
		}catch(WaitTimedOutException e){
			throw new ElementNotExistException("Opção '"+ label +"' não encontrada.\n" +
					"Opções existentes: "+ Arrays.toString(getSel().getSelectOptions(locator)));
		}
	}
	
	@Override
	public void verifyPreviewslyAction() {
		String expectedText = (String)getPreviewslyActionValue();
		if ( expectedText != null)
			verifySelect(expectedText);
	}

	public RequestConfigImp getRequestConfig() {
		return requestDelegate.getRequestConfig();
	}
}
