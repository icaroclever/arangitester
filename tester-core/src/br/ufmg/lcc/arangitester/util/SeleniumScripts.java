package br.ufmg.lcc.arangitester.util;

import java.util.Date;

import com.thoughtworks.selenium.Selenium;

public class SeleniumScripts {
	public static void trocarNomeDeJanelaVazia(Selenium sel){
		long time = new Date().getTime();
		boolean trocar = false;
		for (String nomeJanela: sel.getAllWindowNames()){
			if ( nomeJanela.equals("") ){
				trocar = true;
			}
		}
		if ( trocar ){
			sel.getEval("var win = selenium.browserbot.openedWindows['']; eval('win.name=\"pop" + time + "\"');");
			String script = 
			"var novo = {};"+
			"for (var winName in selenium.browserbot.openedWindows){"+
			"   if ( winName == '' ){"+
			"        novo['pop' + " + time + "] = selenium.browserbot.openedWindows[winName];"+
			"   }else{"+
			"        novo[winName] = selenium.browserbot.openedWindows[winName]; "+
			"   }"+
			"}"+
			"selenium.browserbot.openedWindows = novo;";
			sel.getEval(script);
		}
		
	}
}
