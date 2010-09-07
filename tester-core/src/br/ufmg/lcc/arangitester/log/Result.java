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

/**
 * 
 *  The getTest and setTest methods are used to recover and modify the LccFunctionalSuite object respectively
 *  
 *  The other methods are defined in LccIResult interface.
 */

/**
 * Implement common functionality of LccIResult
 * @author Lucas Gonçalves
 *
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.ufmg.lcc.arangitester.Context;
import br.ufmg.lcc.arangitester.exceptions.ArangiTesterException;
import br.ufmg.lcc.arangitester.interceptors.LoggerImpl;
import br.ufmg.lcc.arangitester.transformers.ResultTransformer;
import br.ufmg.lcc.arangitester.util.StackTraceUtil;

import com.thoughtworks.selenium.Selenium;

public class Result implements IResult {
	private Logger				LOG				= Logger.getLogger(Result.class);

	/*
	 * This variable will contain all informations about the test cases.
	 */
	private FunctionalSuite		test			= new FunctionalSuite();
	private UseCase				currentUseCase	= null;
	private TestCase			currentTestCase	= null;
	private static final String	MSG_PREFIX		= "  ";
	private Error				lastError		= null;

	public void addInfo(String msg) {
		if (currentTestCase == null) {
			msg = "  boot: " + msg;
			String status = msg.endsWith(LoggerImpl.STATUS_OK) ? LoggerImpl.STATUS_OK : LoggerImpl.STATUS_ERROR;
			msg = msg.substring(0, msg.length() - status.length());
			msg = LoggerImpl.getFormatedStatusMsg(msg.trim(), status);
		}

		LOG.info(MSG_PREFIX + msg);
		if (msg.endsWith(LoggerImpl.STATUS_OK)) {
			msg = msg.substring(0, msg.length() - LoggerImpl.STATUS_OK.length());
		}

		Info lccInfo = new Info();
		lccInfo.setMessage(msg);
		if (currentTestCase == null) {
			currentUseCase.addLog(lccInfo);
		} else {
			currentTestCase.addLog(lccInfo);
		}
	}

	public void addError(String msg, Throwable e) {
		if (currentTestCase == null) {
			msg = "  boot: " + msg;
		}

		LOG.error(MSG_PREFIX + msg);

		if (e != null) {
			LOG.error(MSG_PREFIX + e);
			LOG.error("\nStack: " + StackTraceUtil.getStackTrace(e));
		}

		if (msg.endsWith(LoggerImpl.STATUS_ERROR)) {
			msg = msg.substring(0, msg.length() - LoggerImpl.STATUS_ERROR.length());
		}

		Error lccError = new Error();
		lastError = lccError;
		lccError.setCause(msg);
		if (e != null) {
			lccError.setError(e.toString());
		}
		if (currentTestCase == null) {
			currentUseCase.addLog(lccError);
		} else {
			currentTestCase.addLog(lccError);
		}
	}

	public void addError(String msg) {
		addError(msg, null);
	}

	public String getScreenshotName(Date date, Method method) {
		if (date == null) throw new ArangiTesterException("Erro ao criar nome para o screenshot. " +
				"Parametro date não pode ser nulo");

		String methodName = "";
		if (method != null) methodName = method.getName();
		return getScreenshotName(date, methodName);
	}

	public String getScreenshotName(Date date, String name) {
		if (date == null) throw new ArangiTesterException("Erro ao criar nome para o screenshot. " +
				"Parametro date não pode ser nulo");

		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy--kkmmss_SSS");
		return (dateFormat.format(date) + "--" + name);
	}

	public void takeScreenshot(String screenShotName) {
		File diretirioScreenshotHtmls = Context.getInstance().getScreenshotHtmlDir();
		File diretorioScreenShotPngs = Context.getInstance().getScreenshotPngDir();
		Selenium sel = Context.getInstance().getSeleniumController().getSeleniumClient();

		File screenShotHtml = new File(diretirioScreenshotHtmls.getAbsoluteFile(), screenShotName + ".png.html");

		// Saves html screenshot
		try {
			String htmlSource = sel.getHtmlSource();

			OutputStreamWriter f = null;
			if (htmlSource.contains("charset=ISO-8859-1")) {
				f = new OutputStreamWriter(new FileOutputStream(screenShotHtml, false), "ISO-8859-1");
			} else {
				f = new OutputStreamWriter(new FileOutputStream(screenShotHtml, false));
			}

			f.write(htmlSource);
			f.close();
		} catch (IOException e) {
			addError("Erro ao salvar screenshot.\nErro: " + e.getStackTrace());
		}

		// Saves png screenshot
		try {
			sel.captureScreenshot(diretorioScreenShotPngs.getAbsolutePath() + System.getProperty("file.separator") + screenShotName + ".png");
		} catch (Throwable e) {
			addError("Erro ao salvar screenchot em PNG.\nErro: " + e.getStackTrace());
		}
	}

	public void takeAndAddScreenshot(final String screenShotName) {
		if (screenShotName == null) throw new ArangiTesterException("Erro ao tirar screenshot. String screenShotName não pode ser nulo.");

		takeScreenshot(screenShotName);
		addScreenShot(screenShotName + ".png", "info");
		// addScreenShot(screenShotName+".html", "info");
	}

	public void addScreenShot(final String path, final String motivation) {
		String screenshotFullPath = null;

		// Verifica se o screenshot está em formato html ou png, e onde foi salvado
		if (StringUtils.endsWithIgnoreCase(path, "png")) {
			screenshotFullPath = Context.getInstance().getScreenshotPngDir().getAbsolutePath() + System.getProperty("file.separator") + path;
		} else if (StringUtils.endsWithIgnoreCase(path, "html")) {
			screenshotFullPath = Context.getInstance().getScreenshotHtmlDir().getAbsolutePath() + System.getProperty("file.separator") + path;
		} else {
			screenshotFullPath = Context.getInstance().getScreenshotDefaultDir().getAbsolutePath() + System.getProperty("file.separator") + path;
		}

		if (motivation == null || motivation.equals("error")) {
			lastError.setScreenshot(new String(path));
			LOG.error("Screenshot path: " + screenshotFullPath);
		} else {
			LOG.info("** Screenshot tirado: " + screenshotFullPath);
			if (currentTestCase == null) {
				LOG.info("Screenshot não adicionado ao log no formato de xml. CurrentTestCase=null");
			} else {
				currentTestCase.addLog(new Info("** Screenshot tirado: " + screenshotFullPath));
			}

		}
	}

	public void skipTestCase(String method, String name) {
		currentTestCase = new TestCase();
		currentTestCase.setSkip(true);
		currentTestCase.setTestcase(name);
		currentTestCase.setJavaMethod(method);
		LOG.info("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Skiping - " + name + "\n");
		currentTestCase.setStartTime(new Date());
		currentTestCase.setEndTime(new Date());
		currentUseCase.getTestcases().add(currentTestCase);
	}

	public void startTestCase(String methodName, String name) {
		LOG.info("");
		LOG.info("***************** Teste Case '" + name + "' *****************");

		currentTestCase = new TestCase();
		currentTestCase.setTestcase(name);
		currentTestCase.setJavaMethod(methodName);
		currentTestCase.setStartTime(new Date());

		currentUseCase.getTestcases().add(currentTestCase);
	}

	public void endTestCase() {
		currentTestCase.setEndTime(new Date());
		currentTestCase = null;
	}

	/**
	 * This method gets the private LccFunctionalSuite object
	 * 
	 * @return test
	 */
	public FunctionalSuite getTest() {
		return test;
	}

	/**
	 * This method set the private LccFunctionalSuite object
	 * 
	 * @param test
	 */
	public void setTest(FunctionalSuite test) {
		this.test = test;
	}

	public void startUseCase(String name, String[] obs) {
		LOG.info("------------------------------------------------------------------------------------");
		LOG.info("Caso De Uso '" + name + "'");
		LOG.info("------------------------------------------------------------------------------------");

		currentUseCase = new UseCase();
		currentTestCase = null;
		currentUseCase.setName(name);
		currentUseCase.setStartTime(new Date());
		if (obs != null) {
			for (String annotation : obs) {
				addObs(annotation);
			}
		}
		test.getCases().add(currentUseCase);
	}

	public void endUseCase() {
		currentUseCase.setEndTime(new Date());
	}

	@Override
	public void save(File file) {
		XmlResult xml = new XmlResult();
		xml.save(file, getTest());
//		File fileOut = new File(file.getParentFile(), StringUtils.substringBefore(file.getName(), ".") + ".html");
//		try {
//			InputStream xslIs = this.getClass().getClassLoader().getResourceAsStream("toHtml.xsl");
//			ResultTransformer.tranform(file, fileOut, xslIs);
//		} catch (Exception e) {
//			LOG.error("Fail create html result file", e);
//		}
	}

	public void addObs(String annotation) {
		currentUseCase.getObs().add(annotation);
	}
}
