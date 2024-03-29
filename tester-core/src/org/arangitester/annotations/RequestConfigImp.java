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
package org.arangitester.annotations;

import org.arangitester.ui.actions.IRequest.Confirmation;
import org.arangitester.ui.actions.IRequest.Window;

/**
 * Configuration that get a LccRequestConfig and permit change values since Annotations can't change it's values.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class RequestConfigImp {
	private int				forceWait		= 0;
	private Window			window			= Window.CONTINUE;
	private Confirmation	confirmation	= Confirmation.NONE;
	private boolean			ajax			= false;
	private boolean			submit			= true;
	private String			alert			= null;

	public RequestConfigImp() {

	}

	public RequestConfigImp(int wait, Window window, Confirmation confirmation, boolean ajax, boolean submit, String alert) {
		this.forceWait = wait;
		this.window = window;
		this.confirmation = confirmation;
		this.ajax = ajax;
		this.submit = submit;
		this.alert = alert;
	}

	public RequestConfigImp(RequestConfig config) {
		if (config != null) {
			forceWait = config.forceWait();
			window = config.window();
			confirmation = config.confirmation();
			ajax = config.ajax();
			submit = config.submit();
			alert = config.alert();
		}
	}

	public int getForceWait() {
		return forceWait;
	}

	public void setForceWait(int forceWait) {
		this.forceWait = forceWait;
	}

	public Window getWindow() {
		return window;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public Confirmation getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(Confirmation confirmation) {
		this.confirmation = confirmation;
	}

	public boolean isAjax() {
		return ajax;
	}

	public void setAjax(boolean ajax) {
		this.ajax = ajax;
	}

	public boolean isSubmit() {
		return submit;
	}

	public void setSubmit(boolean submit) {
		this.submit = submit;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}
}