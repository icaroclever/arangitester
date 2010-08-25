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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.Page;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.exceptions.EnvException;
import br.ufmg.lcc.arangitester.exceptions.InvokeException;
import br.ufmg.lcc.arangitester.exceptions.TesterException;
import br.ufmg.lcc.arangitester.util.Refletions;
import br.ufmg.lcc.arangitester.util.ArangiTesterStringUtils;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.Wait;
import com.thoughtworks.selenium.Wait.WaitTimedOutException;

public class UiPage extends UiComponent implements IUiComposite {

	public static String	DEFAULT_PAGE_WAIT_TIME	= "50000";	// In miliseconds: 50000 = 50 seconds

	/**
	 * Invoke a url. Url must be Complete. Ex.: http://localhost:8080/app/test.faces
	 * To get http://localhost:8080/app from server.properties use LccContext.getInstance().getConfig().getPath()
	 * 
	 * @param url
	 *            URL requested
	 */
	@Logger("Invoking Url: #0")
	public void invoke(String url) {
		invoke(url, "");
	}

	/**
	 * Invoke a url. Url must be Complete. Ex.: http://localhost:8080/app/test.faces
	 * To get http://localhost:8080/app from server.properties use LccContext.getInstance().getConfig().getPath()
	 * 
	 * @param url
	 *            URL requested
	 * @param title
	 *            Expected page title
	 */
	@Logger("Invoking Url: #0 \tTitle: #1")
	public void invoke(String url, String title) {

		if (title == null)
			throw new InvokeException("Title parameter cannot be null.");

		getSel().open(url);
		getSel().waitForPageToLoad(DEFAULT_PAGE_WAIT_TIME);

		try {
			verifyUrl(url);
			if (!title.equals(""))
				verifyPageTitle(title);
		} catch (Exception e) {
			throw new InvokeException("URL cannot be loaded.");
		}
	}

	/**
	 * Return a instance of interface Selenium. It is the only way for interaction with the browser.
	 * 
	 * @return Selenium instance
	 */
	protected static Selenium getSel() {
		return getInstance().getSeleniumController().getSeleniumClient();
	}

	/**
	 * Closes the current page by clicking on the close button of the browser.
	 */
	@Logger("Closing page")
	public void closePage() {
		getSel().close();
		getSel().selectWindow(null);
	}

	/**
	 * Verify if the loaded page URL is the same of the requested URL
	 * 
	 * @param expectedUrl
	 *            URL requested
	 */
	@Logger("Verifying url: \"#0\" ")
	public void verifyUrl(String expectedUrl) {
		String pageUrl = this.getBrowserUrl();

		if (!pageUrl.contains(expectedUrl)) {
			throw new ArangiTesterException("Current URL is different from expected." +
					"Current URL: " + pageUrl + "\n" +
					"Expected URL: " + expectedUrl);
		}
	}

	/**
	 * Verify if the showed page alert is the expected alert.
	 * 
	 * @param text
	 *            Expected alert text
	 */
	@Logger("Verifying alert: \"#0\" ")
	public void verifyAlert(String text) {
		String alert = getSel().getAlert();
		if (!ArangiTesterStringUtils.containsWithoutSpaces(text, alert)) {
			throw new ArangiTesterException(
					"Current alert dialog is different from expercted. " +
							"Current Alert:" + alert +
							"Expected Alert:" + text);
		}
	}

	/**
	 * Verify if text is present on page.
	 * 
	 * @param expectedText
	 *            text that must be on page.
	 */
	@Logger("Verify if text exist on page: '#0'")
	public void verifyTextPresent(final String expectedText) {
		final String locator = "xpath=/" + this.getXPathLocator();
		final String expectedTextWithoutSpaces = StringUtils.deleteWhitespace(expectedText);
		try {
			waitElement(locator);
			new Wait() {
				@Override
				public boolean until() {
					String html = getSel().getText(locator);
					html = StringUtils.deleteWhitespace(html);
					if (!html.contains(expectedTextWithoutSpaces)) {
						return false;
					} else {
						return true;
					}
				}
			}.wait(locator, UiComponent.DEFAULT_ELEMENT_WAIT_TIME);
		} catch (WaitTimedOutException e) {
			throw new TesterException("Text expected but not found : '" + expectedText + "'");
		}
	}

	/**
	 * Verify if text is NOT present on page.
	 * 
	 * @param unExpectedText
	 *            text that must NOT be on page.
	 */
	@Logger("Verify if text doesnt exist on page: '#0'")
	public void verifyTextNotPresent(final String unExpectedText) {
		final String locator = "xpath=/" + this.getXPathLocator();
		waitElement(locator);
		String html = getSel().getText(locator);
		if (html.contains(unExpectedText)) {
			throw new TesterException("Text unexpected but found: '" + unExpectedText + "'");
		}
	}

	/**
	 * Recursive call verifyPreviewslyAction in all UiComponents on this page.
	 */
	public void verifyAllPreviewslyActions() {
		Iterator<IUiComponent> fullIterator = fullIterator();
		while (fullIterator.hasNext()) {
			fullIterator.next().verifyPreviewslyAction();
		}
	}

	@SuppressWarnings("unchecked")
	public void verifyAllEnabled(boolean enabled, Class... components) {
		for (IUiComponent component : this) {
			if (IUiComposite.class.isAssignableFrom(component.getClass())) {
				((IUiComposite) component).verifyAllEnabled(enabled, components);
			} else {
				component.verifyIsEnable(enabled);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void verifyAllEnable(boolean enable) {
		Class[] components = new Class[] { UiInputText.class, UiCheckBox.class, UiTable.class, UiSelect.class };
		List<? extends IUiComponent> allUiComponents = Refletions.getAllUiComponents(this, components);
		for (IUiComponent component : allUiComponents) {
			if (IUiComposite.class.isAssignableFrom(component.getClass())) {
				((IUiComposite) component).verifyAllEnable(enable);
			} else {
				if (component.exist())
					component.verifyIsEnable(enable);
			}
		}
	}

	/**
	 * Returns the url of the page. Based on Selenium method getUrl().
	 * 
	 * @return Page Location
	 */
	public String getBrowserUrl() {
		return getSel().getLocation();
	}

	/**
	 * This method return the title of the current page which was loaded by Selenium
	 * 
	 * @return Page Title
	 */
	public String getPageTitle() {
		return getSel().getTitle();
	}

	/**
	 * Verify the title of the page.
	 * A blank String is accepted as param too, and will be compared to the
	 * title of the page.
	 * A null param is not accepted, and a exception will be thrown.
	 * 
	 * @param title
	 *            expected title
	 */
	@Logger("Verifying title: #0")
	public void verifyPageTitle(String title) {
		if (title == null) throw new EnvException("Parametro expectedTitle não pode ser nulo!");

		if (!getPageTitle().equalsIgnoreCase(title))
			throw new TesterException(String.format("Current title '%s' is unexpected. The expected title is '%s'", getPageTitle(), title));
	}

	@Override
	public final String getComponentTag() {
		return "html";
	}

	/**
	 * Invokes a page, using values of @Page annotation
	 * */
	public void invoke() {
		invoke(getPageUrl(), getConfig().title());
	}

	/**
	 * Return Page annotated on subclass of UiPage
	 * 
	 * @return null if not exist
	 */
	public Page getConfig() {
		return super.getClass().getSuperclass().getAnnotation(Page.class);
	}

	/**
	 * Returns the url of the page based on annotatin and the context setted
	 * 
	 * @return Url based on the annotation setted on page
	 */
	public String getPageUrl() {

		if (getConfig() == null)
			throw new ArangiTesterException("Annotation cannot be loaded");
		if (Context.getInstance() == null)
			throw new ArangiTesterException("Context instance is null");

		String url = getConfig().url();
		if (!url.startsWith("/"))
			url = "/" + url;
		return Context.getInstance().getConfig().getPath() + url;
	}
}
