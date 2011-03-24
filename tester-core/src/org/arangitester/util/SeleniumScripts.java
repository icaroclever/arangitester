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
package org.arangitester.util;

import java.util.Date;

import com.thoughtworks.selenium.Selenium;

public class SeleniumScripts {
	public static void trocarNomeDeJanelaVazia(Selenium sel) {
		long time = new Date().getTime();
		boolean trocar = false;
		for (String nomeJanela : sel.getAllWindowNames()) {
			if (nomeJanela.equals("")) {
				trocar = true;
			}
		}
		if (trocar) {
			sel.getEval("var win = selenium.browserbot.openedWindows['']; eval('win.name=\"pop" + time + "\"');");
			String script =
					"var novo = {};" +
							"for (var winName in selenium.browserbot.openedWindows){" +
							"   if ( winName == '' ){" +
							"        novo['pop' + " + time + "] = selenium.browserbot.openedWindows[winName];" +
							"   }else{" +
							"        novo[winName] = selenium.browserbot.openedWindows[winName]; " +
							"   }" +
							"}" +
							"selenium.browserbot.openedWindows = novo;";
			sel.getEval(script);
		}

	}
}
