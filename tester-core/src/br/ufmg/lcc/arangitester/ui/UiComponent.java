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
package br.ufmg.lcc.arangitester.ui;

import static br.ufmg.lcc.arangitester.Context.getInstance;

import java.lang.annotation.Annotation;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.Ui;
import br.ufmg.lcc.arangitester.annotations.VerifyAjax;
import br.ufmg.lcc.arangitester.exceptions.ElementNotExistException;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.exceptions.WrongValueException;
import br.ufmg.lcc.arangitester.ui.actions.IUiClickable;
import br.ufmg.lcc.arangitester.ui.iterators.ComponentsIterator;
import br.ufmg.lcc.arangitester.ui.iterators.FullComponentsIterator;
import br.ufmg.lcc.arangitester.ui.iterators.FullComponentsIteratorWithFirstRealLineOfTables;
import br.ufmg.lcc.arangitester.ui.iterators.FullComponentsIteratorWithRealLinesOfTable;
import br.ufmg.lcc.arangitester.util.ArangiTesterStringUtils;
import br.ufmg.lcc.arangitester.util.ArangiTesterWait;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

/**
 * It is base for all Ui Component.
 * Use Composite pattern.
 * 
 * @author Lucas Gonçalves
 * 
 */
public abstract class UiComponent implements IUiComponent {

	public static Long			DEFAULT_ELEMENT_WAIT_TIME	= 10000L;						// Number in mileseconds: 10000L = 10 seconds

	private IUiComponent		parent;
	protected IComponentLocator	locator						= LocatorFactory.getLocator();

	private Annotation[]		configs;

	private String				componentLocator;
	private String				componentDesc;
	private String				componentId;
	private String				componentName;
	private String				componentIndex;

	private Object				previewslyActionValue;

	@SuppressWarnings("unchecked")
	public <T extends Annotation> T getConfig(Class<T> annotation) {
		if (configs != null) for (Annotation config : configs) {
			if (annotation.isAssignableFrom(config.getClass())) {
				return (T) config;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Logger("Clicking at:  #{componentDesc}")
	public void click() {
		String xpath = this.getComponentLocator();
		this.waitElement(xpath);
		getSel().click(xpath);
	}

	/**
	 * {@inheritDoc}
	 */
	@Logger("Double Clicking at:  #{componentDesc}")
	public void doubleClick() {
		String xpath = this.getComponentLocator();
		this.waitElement(xpath);
		getSel().click(xpath);
		getSel().doubleClick(xpath);
	}

	public void setConfigs(Annotation[] configs) {
		this.configs = configs;
		Ui basicConfig = getConfig(Ui.class);
		setComponentLocator(basicConfig.locator());
		setComponentDesc(basicConfig.desc());
		setComponentId(basicConfig.id());
		setComponentName(basicConfig.name());
		setComponentIndex(basicConfig.index());
	}

	/**
	 * Move mouse over the component.
	 */
	@Logger("Mouser Over:  #{componentDesc}")
	public void mouseOver() {
		waitElement(getComponentLocator());
		getSel().mouseOver(getComponentLocator());
	}

	/**
	 * Verify if element exist, if expectation is true. Or element not exist, if expectation is false.
	 * 
	 * @param expected
	 *            Their expectation on the existence of element
	 * @throws ElementNotExistException
	 */
	@Logger("Verifyng if element exists(#0):  #{componentDesc}")
	public void verifyExist(boolean expected) {
		try {
			waitElement(getComponentLocator());
		} catch (ElementNotExistException e) {
			if (expected) throw new ElementNotExistException("Elemento " + getComponentDesc() + " não presente, quando deveria estar.");
			return;
		}
		if (!expected) throw new ArangiTesterException("Elemento " + getComponentDesc() + " presente, quando NÃO deveria estar.");
	}

	@Logger("Verifyng Text on #{componentDesc}: \"#0\"")
	public void verifyTextInside(final String expectedText) {
		verifyTextInsideWithoutLog(expectedText);
	}

	public void verifyTextInsideWithoutLog(final String expectedText) {
		if (!isTextInside(expectedText)) {
			final String texto = getSel().getText(getComponentLocator());
			throw new WrongValueException("\nTexto esperado mas não presente: '" + expectedText + "'\nTexto presente: '" + getText() + "'", expectedText, texto);
		}
	}

	@Logger("Verifyng text not present on #{componentDesc}: \"#0\"")
	public void verifyTextNotPresent(final String expectedText) {
		if (isTextInside(expectedText)) throw new ArangiTesterException("Texto não deveria estar presente, mas está: " + expectedText);
	}

	public boolean isTextInside(final String expectedText) {
		if (this instanceof UiPage) {
			throw new TesterException("Este método não deve ser chamado em uma página. Utilize verifyTextPresent(txt)");
		}

		waitElement(getComponentLocator());

		return new ArangiTesterWait(DEFAULT_ELEMENT_WAIT_TIME) {
			@Override
			public boolean until() {
				final String texto = getSel().getText(getComponentLocator());
				return ArangiTesterStringUtils.containsWithoutSpaces(texto, expectedText);
			}
		}.getCondition();
	}

	public boolean isTextInsideWithoutWait(final String expectedText) {
		final String texto = getSel().getText(getComponentLocator());
		return ArangiTesterStringUtils.containsWithoutSpaces(texto, expectedText);
	}

	public Iterator<IUiComponent> iterator() {
		return new ComponentsIterator(this);
	}

	public Iterator<IUiComponent> fullIterator() {
		return new FullComponentsIterator(this);
	}

	public Iterator<IUiComponent> fullWithClosedTableItesIterator() {
		return new FullComponentsIteratorWithRealLinesOfTable(this);
	}

	public Iterator<IUiComponent> singleWithClosedTableItemsIterator() {
		return new FullComponentsIteratorWithFirstRealLineOfTables(this);
	}

	public void verifyPreviewslyAction() {

	}

	public void validate(IUiClickable saveButton, IUiComponent divMessage) {

	}

	public void clearPreviewslyAction() {
		previewslyActionValue = null;
	}

	@VerifyAjax
	protected void waitElement(final String loc) {
		try {
			new Wait() {
				@Override
				public boolean until() {
					return getSel().isElementPresent(loc);
				}
			}.wait(loc, DEFAULT_ELEMENT_WAIT_TIME);
		} catch (Throwable e) {
			throw new ElementNotExistException("Elemento " + loc + " não presente.", e);
		}
	}

	@Logger("Verify if #{componentDesc} is enable(#0)")
	@VerifyAjax
	public void verifyIsEnable(final boolean enable) {
		try {
			new Wait() {
				@Override
				public boolean until() {
					if (enable) {
						return getSel().isEditable(getComponentLocator()) && !isReadOnly();
					} else {
						return !getSel().isEditable(getComponentLocator()) || !isReadOnly();
					}
				}
			}.wait("", DEFAULT_ELEMENT_WAIT_TIME);
		} catch (Throwable e) {
			if (enable) {
				throw new WrongValueException("Element is NOT editable but should be.");
			} else {
				throw new WrongValueException("Element is Editable but shouldn´t be.");
			}
		}
	}

	@Logger("Verify if #{componentDesc} is visible(#0)")
	public void verifyIsVisible(boolean expected) {
		waitElement(getComponentLocator());
		boolean visible = getSel().isVisible(getComponentLocator());

		if (visible && !expected) {
			throw new WrongValueException("Element is visible, but it shouldn't be.");
		} else if (!visible && expected) {
			throw new WrongValueException("Element isn't visible, but it should be.");
		}
	}

	public boolean isEnable() {
		waitElement(getComponentLocator());
		return (getSel().isEditable(getComponentLocator()) && !isReadOnly());
	}

	@Logger("Verify if #{componentDesc} is visible")
	public boolean isVisible() {
		waitElement(getComponentLocator());
		return (getSel().isVisible(getComponentLocator()));
	}

	private boolean isReadOnly() {
		try {
			return getSel().getAttribute(getComponentLocator() + "@readonly").equals("readonly");
		} catch (SeleniumException e) {

		}

		return false;
	}

	public boolean exist() {
		return getSel().isElementPresent(getComponentLocator());
	}

	public String getText() {
		return getSel().getText(this.getComponentLocator());
	}

	protected static Selenium getSel() {
		return getInstance().getSeleniumController().getSeleniumClient();
	}

	public void setParent(IUiComponent parent) {
		this.parent = parent;
	}

	public IUiComponent getParent() {
		return parent;
	}

	public String getComponentDesc() {
		return componentDesc;
	}

	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}

	public String getComponentLocator() {
		return locator.getComponentLocator(componentLocator, this);
	}

	public void setComponentLocator(String componentlocator) {
		this.componentLocator = componentlocator;
	}

	public Object getPreviewslyActionValue() {
		return previewslyActionValue;
	}

	public void setPreviewslyActionValue(Object previewslyActionValue) {
		this.previewslyActionValue = previewslyActionValue;
	}

	public String getComponentId() {
		return locator.getComponentId(componentId, this);
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getComponentName() {
		return locator.getComponentName(componentName, this);
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getComponentIndex() {
		return componentIndex;
	}

	public void setComponentIndex(String componentIndex) {
		this.componentIndex = componentIndex;
	}

	/**
	 * Component HTML tag representation
	 * 
	 * @return HTML tag representation
	 */
	public String getComponentTag() {
		return null;
	}

	public static Long getDEFAULT_ELEMENT_WAIT_TIME() {
		return DEFAULT_ELEMENT_WAIT_TIME;
	}

	public static void setDEFAULT_ELEMENT_WAIT_TIME(Long default_element_wait_time) {
		DEFAULT_ELEMENT_WAIT_TIME = default_element_wait_time;
	}

	/**
	 * Gets a xPath locator using the component's id, name or index .
	 * 
	 * @return Xpath Locator.
	 */
	public String getXPathLocator() {

		if (!StringUtils.isNotBlank(this.getComponentTag())) throw new TesterException("Component tag has not found.");

		if (StringUtils.isNotBlank(this.getComponentId()))
			return String.format("/%s%s[@id='%s']", this.locator.getHtmlNameSpace(), this.getComponentTag(), this.getComponentId());
		else if (StringUtils.isNotBlank(this.getComponentName()))
			return String.format("/%s%s[@name='%s']", this.locator.getHtmlNameSpace(), this.getComponentTag(), this.getComponentName());
		else if (StringUtils.isNotBlank(this.getComponentIndex()))
			return String.format("/%s%s[%d]", this.locator.getHtmlNameSpace(), this.getComponentTag(), this.getComponentIndex());
		else {
			return String.format("/%s%s", this.locator.getHtmlNameSpace(), this.getComponentTag());
		}
	}
}
