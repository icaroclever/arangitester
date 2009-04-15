package br.ufmg.lcc.arangitester.ui;

import java.util.ArrayList;
import java.util.List;

import br.ufmg.lcc.arangitester.annotations.Logger;
import br.ufmg.lcc.arangitester.annotations.TabDef;
import br.ufmg.lcc.arangitester.annotations.TabDefs;
import br.ufmg.lcc.arangitester.exceptions.EnvException;
import br.ufmg.lcc.arangitester.ioc.ICreate;

public class UiTabs extends UiClickable implements ICreate{
	private List<String> ids = new ArrayList<String>();
	private List<String> names = new ArrayList<String>();
	
	
	/**
	 * Click on a tab.
	 * @param number Number of tab starting with 1
	 */
	@Logger("Clicking on tab #{componentDesc}")
	public void click(int number) {
		setComponentId(ids.get(number - 1));
		setComponentDesc(names.get(number - 1));
		getRequestConfig().setAjax(true);
		getRequestConfig().setForceWait(2000);
		super.click();
		System.out.println("Clicando na tela " + number);
	}
	
	/**
	 * Click on a tab.
	 * @param Name of a tab. If the tab does not exist, nothing happen.
	 */
	public void click(String name) {
		int tabIndex = 0;
		boolean hasFound = false;

		for (String element : names) {
			if(element.equalsIgnoreCase(name)){
				hasFound = true;
				break;
			}
			tabIndex++;
		}
		
		if( hasFound ) {
			click(tabIndex+1);
		}else{
			System.err.println("Nao ha aba com esse nome! " + name);
			throw new EnvException("Não foi encontrada a tab com o nome: "+ name +". Verifique o nome bla");
		}
	}	
	
	public void addTab(String id, String name){
		ids.add(id);
		names.add(name);
	}
	
	@Override
	public void verifyIsEnable(boolean enable) {
	}

	@Override
	public void create() {
		super.create();
		TabDefs tabs = super.getConfig(TabDefs.class);
		for (TabDef def: tabs.value()){
			addTab(def.id(), def.name());
		}
		getRequestConfig().setSubmit(false);
		getRequestConfig().setAjax(true);
		getRequestConfig().setForceWait(1500);
	}
}
