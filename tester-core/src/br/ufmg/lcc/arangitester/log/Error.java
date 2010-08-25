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

public class Error {
	private String	cause;		// Error cause
	private String	error;		// Stack Trace
	private String	screenshot; // Path of the Screenshot

	/**
	 * Get the cause of the error
	 * 
	 * @return cause
	 */
	public String getCause() {
		return cause;
	}

	/**
	 * Set the object cause variable
	 * 
	 * @param cause
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}

	/**
	 * Get the object stack trace
	 * 
	 * @return error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Set the object stack trace variable
	 * 
	 * @param error
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * Get the object's screenshot path
	 * 
	 * @return screenshot
	 */
	public String getScreenshot() {
		return screenshot;
	}

	/**
	 * Set the object's screenshot path
	 * 
	 * @param screenshot
	 */
	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

}
