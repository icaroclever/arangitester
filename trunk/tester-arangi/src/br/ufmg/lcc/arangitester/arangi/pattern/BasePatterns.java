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
package br.ufmg.lcc.arangitester.arangi.pattern;

import java.lang.annotation.Annotation;

import javax.el.ExpressionFactory;
import javax.el.PropertyNotFoundException;
import javax.el.ValueExpression;

import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.ITestCase;
import br.ufmg.lcc.arangitester.arangi.annotations.Field;
import br.ufmg.lcc.arangitester.arangi.annotations.PopField;
import br.ufmg.lcc.arangitester.arangi.annotations.PopUp;
import br.ufmg.lcc.arangitester.arangi.pages.ArangiSearchPage;
import br.ufmg.lcc.arangitester.arangi.pages.NullSearchPage;
import br.ufmg.lcc.arangitester.el.CompositeResolver;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.ioc.UiComponentFactory;
import br.ufmg.lcc.arangitester.ui.IUiComponent;
import br.ufmg.lcc.arangitester.ui.UiButton;
import br.ufmg.lcc.arangitester.ui.UiCheckBox;
import br.ufmg.lcc.arangitester.ui.UiInputText;
import br.ufmg.lcc.arangitester.ui.UiRadio;
import br.ufmg.lcc.arangitester.ui.UiSelect;
import br.ufmg.lcc.arangitester.ui.UiTab;
import br.ufmg.lcc.arangitester.ui.actions.IRequest;
import br.ufmg.lcc.arangitester.ui.actions.UiClickable;

import com.thoughtworks.selenium.Selenium;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

/**
 * Base class for Tests Patterns. It implement generic method to help fill and read
 * fields.
 * @author Lucas Gonçalves
 *
 */
public abstract class BasePatterns implements ITestCase{
	private Logger LOG = Logger.getLogger(BasePatterns.class);
	public static enum ACTION{
		MODIFY, CANCEL, ADD
	}
	
	protected ExpressionFactory factory = new ExpressionFactoryImpl();
	protected SimpleContext context = new SimpleContext(new CompositeResolver());
	
	protected <T> T createPage(Class<T> pageClass, String pageName){
		T page = UiComponentFactory.getInstance(pageClass);
		context.setVariable(pageName, factory.createValueExpression(page, pageClass));
		return page;
	} 

	public boolean isToExecute(String methodName){
		return true;
	}
	
	/**
	 * Resolve a El express.
	 * if you want a object ${mypage.nome} on expression path 'nome' and on pageName 'mypage'
	 * 
	 * @param exprStr expression without ${} and without the name of main object, on common case it is the page.
	 * 
	 * @param pageName name of the main object.
	 * @return A object or null if it not exist.
	 */
	protected IUiComponent resolveElExpression(String exprStr, String pageName){
		ValueExpression expr =	factory.createValueExpression(context, "${" + pageName + "." + exprStr +"}", Object.class);
		IUiComponent value = null;
		try{
			value = (IUiComponent)expr.getValue(context);
		}catch (PropertyNotFoundException e){
			return null;
		}
		return value;
	}
	
	protected boolean isPopup(Field fieldConfig){
		if ( fieldConfig.popup()!= null && fieldConfig.popup().searchPage() != NullSearchPage.class){
			return true;
		}
		return false;
	}
	
	/**
	 * Fills some field, considering the action happening(inserting a registry,
	 * modifying a registry, etc <br/>
	 * @param target Target element of the page, the element wich we will 
	 * 			type something, or select something, etc.
	 * @param fieldConfig We will get the values that we will type or select from
	 * 			this param. It consists of the annotations of the page.
	 * @param action If it is an action of add a registry in the system, cancel 
	 * 			an addition or modify a registry
	 */
	protected void fill(Object target, Field fieldConfig, ACTION action){
		LOG.debug("Preenchendo valor do campo: " + fieldConfig.name());
		boolean closed = false;
		
		if ( isPopup(fieldConfig) ){
			int tryCount = 0;
			for(tryCount=0; tryCount < 2 && !closed; tryCount++){
				closed = true;
				fillPopup(target, fieldConfig, action);
				int allOpenedWindowsNumber = getSel().getAllWindowIds().length;
				
				for(int i=1; i < allOpenedWindowsNumber;i++)
				{
					if(getSel().getAllWindowNames()[i].equals("null"))
						continue;
					getSel().selectWindow(getSel().getAllWindowNames()[i]);
					getSel().windowFocus();
					getSel().close();
					closed = false;
					LOG.warn("Selenium had a problem with the javascript page.");
				}
				getSel().selectWindow("null");
				getSel().windowFocus();
			}
			
			if(tryCount == 2 && closed == false)
			{
				throw new ArangiTesterException("The page cannot be closed");
			}
			
			return;
		}
		
		String actionValue = null;
		if ( action == ACTION.ADD){
			actionValue = fieldConfig.addValue();
		}else if ( action == ACTION.CANCEL){
			actionValue = fieldConfig.cancelValue();
		}else if ( action == ACTION.MODIFY){
			actionValue = fieldConfig.modifyValue();
		}

		if ( target instanceof UiInputText){
			((UiInputText)target).type(actionValue);
		}else if ( target instanceof UiSelect){
			((UiSelect)target).select(actionValue);
		}else if (target instanceof UiCheckBox) {
			if (actionValue.toLowerCase().equals("sim"))
				((UiCheckBox)target).check();
			else
				((UiCheckBox)target).uncheck();
		}else if (target instanceof UiRadio){
			((UiRadio)target).select(actionValue);
		}else if (target instanceof UiButton ){
			((UiButton)target).click();
		}else if (target instanceof UiTab ){
			if( actionValue.toLowerCase().equals("click"))
				((UiTab)target).click();
		}
	}
	
	/**
	 * Fills some field, considering the action happening(inserting a registry,
	 * modifying a registry, etc <br/>
	 * @param target Target element of the page, the element wich we will 
	 * 			type something, or select something, etc.
	 * @param fieldConfig We will get the values that we will type or select from
	 * 			this param. It consists of the annotations of the page.
	 * @param action If it is an action of add a registry in the system, cancel 
	 * 			an addition or modify a registry
	 */
	protected void compare(Object target, Field fieldConfig, ACTION action){
		LOG.debug("Preenchendo valor do campo: " + fieldConfig.name());
		if ( isPopup(fieldConfig) ){
			fillPopup(target, fieldConfig, action);
			return;
		}
		
		String actionValue = null;
		if ( action == ACTION.ADD){
			actionValue = fieldConfig.addValue();
		}else if ( action == ACTION.CANCEL){
			actionValue = fieldConfig.cancelValue();
		}else if ( action == ACTION.MODIFY){
			actionValue = fieldConfig.modifyValue();
		}

		if ( target instanceof UiInputText){
			((UiInputText)target).verifyTextInside(actionValue);
		}else if ( target instanceof UiSelect){
			((UiSelect)target).verifySelect(actionValue);
		}else if (target instanceof UiCheckBox) {
			((UiCheckBox)target).verifyCheckBox(actionValue);
		}else if (target instanceof UiRadio){
			((UiRadio)target).verifySelect(actionValue);
		}
	}

	protected void fillPopup(Object target, Field fieldConfig, ACTION action){
		UiClickable clicableComponent = (UiClickable)target;
		clicableComponent.getRequestConfig().setWindow(IRequest.Window.OPEN);
		clicableComponent.click();
		ArangiSearchPage searchPage = createPage(fieldConfig.popup().searchPage(), "PopSearch");
		for (PopField field: fieldConfig.popup().popFields()){
			IUiComponent targetField = resolveElExpression(field.name(), "PopSearch");
			if (target != null){
				fill(targetField, new FieldImpl(field), action);
			}
		}
		searchPage.getBtnSearch().click();
		searchPage.getResult().getLine(0).getSelect().click();
	}
	
	protected void verify(Object target, String value){
		if(target == null && value == null)
			return;
		else if ( target instanceof UiInputText){
			((UiInputText)target).verifyValue(value);
		}else if (target instanceof UiSelect){
			((UiSelect)target).verifySelect(value);
		}else if (target instanceof UiCheckBox){
			((UiCheckBox)target).verifyCheckBox(value);
		}else if (target instanceof UiTab){
			((UiTab)target).clickWithOutLogger();
		}
	}
	
	protected String getValue(IUiComponent target){
		if ( target instanceof UiInputText){
			return ((UiInputText)target).getValue();
		}else if (target instanceof UiSelect){
			return ((UiSelect)target).getSelectedLabel();
		}else if (target instanceof UiCheckBox){
			return ((UiCheckBox)target).getStatus();
		} else if (target instanceof UiTab){
			((UiTab)target).clickWithOutLogger();
		}
		
		return null;
	}
	
	protected Selenium getSel(){
		return Context.getInstance().getSeleniumController().getSeleniumClient();
	}
	
}


class FieldImpl implements Field {
	private String name;
	private String addValue;
	private String cancelValue;
	private String modifyValue;

	public FieldImpl(PopField popField){
		this.name = popField.name();
		this.addValue = popField.addValue();
		this.cancelValue = popField.cancelValue();
		this.modifyValue = popField.modifyValue();
	}
	
	@Override
	public String addValue() {
		return addValue;
	}

	@Override
	public String cancelValue() {
		return cancelValue;
	}

	@Override
	public String modifyValue() {
		return modifyValue;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public PopUp popup() {
		return null;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return null;
	}
	
}
