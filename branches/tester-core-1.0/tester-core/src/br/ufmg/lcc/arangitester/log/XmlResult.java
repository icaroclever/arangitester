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
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlResult {

	private String getFormatedDate(Date date) {
		return DateFormat.getTimeInstance().format(date);
	}

	public void save(File file, FunctionalSuite result) {

		// file indicates a pathname
		Document document = DocumentHelper.createDocument(); // create a new xml document
		Element root = document.addElement("LccFunctionalSuite"); // create the root tag named LccFunctionalSuite
		if (result == null)
			return;

		SummaryHelper summary = new SummaryHelper(result);

		root.addAttribute("totalTime", String.valueOf(summary.getTotalTime()));
		System.out.println("Tempo de Execução: " + summary.getTotalTime() + " min");
		root.addAttribute("total", String.valueOf(summary.getTotal()));
		System.out.println("Total: " + summary.getTotal());
		root.addAttribute("skip", String.valueOf(summary.getSkip()));
		System.out.println("Skiped: " + summary.getSkip());
		root.addAttribute("fail", String.valueOf(summary.getFail()));
		System.out.println("Fail: " + summary.getFail());
		root.addAttribute("successful", String.valueOf(summary.getSuccessful()));
		System.out.println("Successful: " + summary.getSuccessful());
		root.addAttribute("percent", String.valueOf(summary.getPercent()));
		System.out.println("Sucessful: " + summary.getPercent() + "%");

		for (UseCase usecase : result.getCases()) {
			Element userCaseElement = root.addElement("UseCase")
												.addAttribute("name", usecase.getName())
												.addAttribute("startTime", getFormatedDate((usecase.getStartTime())))
												.addAttribute("endTime", getFormatedDate(usecase.getEndTime()));

			for (String obs : usecase.getObs()) {
				Element obsElement = userCaseElement.addElement("Obs");
				obsElement.addText(obs);
			}

			for (Object log : usecase.getlogs()) {
				if (log instanceof Info) {
					userCaseElement.addElement("info").addText(((Info) log).getMessage());
				} else {
					Error error = ((Error) log);
					Element errorElement = userCaseElement.addElement("error").addAttribute("cause", error.getCause());
					if (error.getScreenshot() != null)
						errorElement.addAttribute("screenshot", error.getScreenshot());

					if (error.getError() != null)
						errorElement.addText(error.getError());
				}
			}

			for (TestCase testcase : usecase.getTestcases()) {
				Element testCaseElement = userCaseElement.addElement("TestCase")
																.addAttribute("name", testcase.getJavaMethod())
																.addAttribute("description", testcase.getTestcase())
																.addAttribute("startTime", getFormatedDate(testcase.getStartTime()))
																.addAttribute("endTime", getFormatedDate(testcase.getEndTime()))
																.addAttribute("skip", String.valueOf(testcase.isSkip()));

				for (Object log : testcase.getlogs()) {
					if (log instanceof Info) {
						testCaseElement.addElement("info").addText(((Info) log).getMessage());
					} else {
						Error error = ((Error) log);
						Element errorElement = testCaseElement.addElement("error").addAttribute("cause", error.getCause());
						if (error.getScreenshot() != null)
							errorElement.addAttribute("screenshot", error.getScreenshot());

						if (error.getError() != null)
							errorElement.addText(error.getError());
					}
				}

			}
		}
		// End of the document building
		// Now, we will start to write in document
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			OutputFormat outformat = OutputFormat.createPrettyPrint();
			XMLWriter write = new XMLWriter(new FileWriter(file), outformat); // Initialize the xml file
			write.write(document); // Write the final document on xml file
			write.close();
		} catch (IOException e) {
			System.out.println("Erro durante a gravação no arquivo " + file + " :\n" + e.toString());
		}

	}
}
