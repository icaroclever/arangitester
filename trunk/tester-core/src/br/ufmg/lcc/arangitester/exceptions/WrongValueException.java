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
package br.ufmg.lcc.arangitester.exceptions;

/**
 * Used on verification of fields. If the value of field is not expected to be that
 * then throw this exception
 * @author Lucas Gonçalves
 *
 */
public class WrongValueException extends LccException {

	private static final long serialVersionUID = 1L;
	private String actualValue;
	private String expectedValue;
	
	public WrongValueException(String msg) {
		super(msg);
	}
	
	public WrongValueException(String msg, String expectedValue, String actualValue) {
		super(msg);
		this.expectedValue = expectedValue;
		this.actualValue = actualValue;
	}

	public String getActualValue() {
		return actualValue;
	}

	public String getExpectedValue() {
		return expectedValue;
	}
}
