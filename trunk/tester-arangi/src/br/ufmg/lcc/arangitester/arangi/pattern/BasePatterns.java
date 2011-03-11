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
import java.util.Stack;

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
	
	protected boolean isPopup(FieldImpl fieldConfig){
		if ( fieldConfig.popup()!= null && fieldConfig.popup().searchPage() != NullSearchPage.class){
			return true;
		}
		return false;
	}
	
	private Stack<String> windowNameList()
	{
		Stack<String> windowNames = new Stack<String>();
		String[] allWindowNames = getSel().getAllWindowNames();
		
		for(int i=0; i < allWindowNames.length;i++)
		{
			if(!allWindowNames[i].equals("null"))
				windowNames.push((allWindowNames[i].startsWith("selenium"))?"null":allWindowNames[i]);
		}
		
		return windowNames;
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
	protected void fill(IUiComponent target, FieldImpl fieldConfig, ACTION action){
		LOG.debug("Preenchendo valor do campo: " + fieldConfig.name());
		
		if (!isPopup(fieldConfig))
		{	
			String actionValue = null;
			switch(action)
			{
				case ADD: 	 actionValue = fieldConfig.addValue();
					break;
				case CANCEL: actionValue = fieldConfig.cancelValue();
					break;
				case MODIFY: actionValue = fieldConfig.modifyValue();
					break;
			}
	
			if ( target instanceof UiInputText){
				((UiInputText)target).type(actionValue);
			}else if ( target instanceof UiSelect){
				((UiSelect)target).select(actionValue);
			}else if (target instanceof UiCheckBox) {
				if (actionValue.toLowerCase().equals("checked"))
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
		}else{
			fillPopup(target, fieldConfig, action);
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
	protected void compare(Object target, FieldImpl fieldConfig, ACTION action){
		LOG.debug("Preenchendo valor do campo: " + fieldConfig.name());
		if ( isPopup(fieldConfig) ){
			fillPopup(target, fieldConfig, action);
		} else{
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
	}

	protected void fillPopup(Object target, FieldImpl fieldConfig, ACTION action){
		Stack<String> oldWNList = windowNameList();
		UiClickable clicableComponent = (UiClickable)target;
		clicableComponent.getRequestConfig().setWindow(IRequest.Window.OPEN);
		clicableComponent.click();
		
		getSel().selectWindow(windowNameList().peek());
		getSel().windowFocus();
		
		ArangiSearchPage searchPage = createPage(fieldConfig.popup().searchPage(), "PopSearch");
		for (PopField field: fieldConfig.popup().popFields()){
			IUiComponent targetField = resolveElExpression(field.name(), "PopSearch");
			if (target != null){
				getSel().windowFocus();
				fill(targetField, new FieldImpl(field), action);
			}
		}
		
		if(searchPage.getBtnSearch().exist())
		{
			searchPage.getBtnSearch().click();
		}
		
		selectResult(searchPage);
		
		Stack<String> currentWNList = windowNameList();
		if(!oldWNList.containsAll(currentWNList))
		{
			throw new ArangiTesterException("The page cannot be closed");
		}
		getSel().selectWindow(currentWNList.pop());
		getSel().windowFocus();
	}
	
	
	/**
	 * This method execute before the items search. It select the first item occurrence in the result. 
	 * @param searchPage
	 */
	protected void selectResult(ArangiSearchPage searchPage)
	{
		if(searchPage.getResult().getRealLinesNumber() != 0)
			searchPage.getResult().getLine(0).getSelect().click();
		else
			throw new ArangiTesterException("The result is empty.");
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

class FieldImpl{
	private String name;
	private String addValue;
	private String cancelValue;
	private String modifyValue;
	private PopUp popup;

	public FieldImpl(Field field){
		this.name = field.name();
		this.addValue = field.addValue();
		this.cancelValue = field.cancelValue();
		this.modifyValue = field.modifyValue();
		this.popup = field.popup();
	}
	
	public FieldImpl(PopField popField){
		this.name = popField.name();
		this.addValue = popField.addValue();
		this.cancelValue = popField.cancelValue();
		this.modifyValue = popField.modifyValue();
	}
	
	
	public String addValue() {
		return addValue;
	}

	public String cancelValue() {
		return cancelValue;
	}

	public String modifyValue() {
		return modifyValue;
	}

	
	public String name() {
		return name;
	}

	public PopUp popup() {
		return popup;
	}

	public Class<? extends Annotation> annotationType() {
		return null;
	}

	public boolean noVerify() {
		return false;
	}
	
}
