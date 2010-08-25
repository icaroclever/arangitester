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
package br.ufmg.lcc.arangitester.log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * All information about execution of a test script will be routed to a sub class of LccIResult.
 * 
 * @author Lucas Gonçalves
 * 
 */
public interface IResult {
	/**
	 * Log a simple information, like 'Clicking on button x',
	 * 'Typing on field x'
	 * 
	 * @param msg
	 *            Message to log
	 */
	void addInfo(String msg);

	/**
	 * Log a error, like 'Field not present on page'
	 * 
	 * @param msg
	 *            Error msg
	 * @param e
	 *            Error Stack trace to log
	 */
	void addError(String msg, Throwable e);

	/**
	 * Log a error, like 'Field not present on page'
	 * 
	 * @param msg
	 *            Error msg
	 */
	void addError(String msg);

	/**
	 * Gets the screenshot name, using the time and current method to compose
	 * the name.
	 * 
	 * @param date
	 *            An object date representing the actual time. Cannot be null.
	 * @param method
	 *            The method that called the screenshot. If its null, it wont
	 *            be used.
	 * @return The name of the screenshot.
	 *         The name will be like this:<br/>
	 *         25112008--150630_828--verifyTextInside
	 */
	public String getScreenshotName(Date date, Method method);

	/**
	 * Gets the screenshot name, using the time and a name to compose the name.
	 * 
	 * @param date
	 *            An object date representing the actual time. Cannot be null.
	 * @param name
	 *            A string to compose the name of screenshot
	 * @return The name of the screenshot <br\>
	 *         The name will be like this: 25112008--150630_828--name
	 */
	public String getScreenshotName(Date date, String name);

	/**
	 * Take a screenshot of the browser, in html and png format, and save
	 * the files in the screenshot directory, which is setted on LccContext class.
	 * This method doens't add this information to the log. To do so, you must
	 * call addScreenshot method of this class.
	 * 
	 * @param screenShotName
	 *            The name of the screenshot file, without the extension.
	 *            Thus, the files created will be screenShotName.html and screenShotName.png
	 */
	public void takeScreenshot(String screenShotName);

	/**
	 * Takes a screenshot and adds this information to the log.
	 * 
	 * @param screenShotName
	 *            The name of the screenshot WITHOUT any file extension
	 * @see takeScreenshot
	 */
	public void takeAndAddScreenshot(String screenShotName);

	/**
	 * Add a screenshot file after a unsuccessful test, or to get
	 * more information of the test.
	 * 
	 * @param path
	 *            : The name of the screenshot file, WITH the extension. Like screen123.png
	 * @param motivation
	 *            : The motivation of the screenshot, that will tell how
	 *            to log it. Can be a one of two Strings: "error" or "info".
	 *            If this param does not match any of those, the default is info.
	 * 
	 */
	void addScreenShot(String path, String motivation);

	/**
	 * Skip a test case
	 * 
	 * @param name
	 *            of test case to be skiped
	 */
	public void skipTestCase(String method, String name);

	/**
	 * When a new test script start, this method must be called.
	 * Next info and error messages will be associete with this new testcase.
	 * 
	 * @param name
	 *            Simbolic name for the test case
	 */
	void startTestCase(String methodName, String name);

	/**
	 * When a test script finish, this method must be called.
	 * It saves the end time in the appropriate field in test case object.
	 * If this method is call before a startTestCase, an error is throw.
	 */
	void endTestCase();

	/**
	 * Save test case to a file. The subclasses can save on diferent types of files.
	 * 
	 * @param file
	 *            File to be saved.
	 */
	void save(File file);

	/**
	 * When a new use case start, this method must be called.
	 * For each use case, we can have many differents test cases.
	 * This method also save the use case start time and its annotations.
	 * 
	 * @param name
	 *            Simbolic name for the use case
	 * @param obs
	 *            List of observations which will save in UseCase object
	 */
	void startUseCase(String name, String[] obs);

	/**
	 * When a use case finish, this method must be called.
	 * It saves the end time in the appropriate field in use case object.
	 * If this method is call before a startUseCase, an error is throw.
	 */
	void endUseCase();

	/**
	 * Add an observation to a UseCase object
	 * 
	 * @param annotation
	 */
	void addObs(String annotation);
}
