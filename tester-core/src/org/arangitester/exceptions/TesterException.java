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
package org.arangitester.exceptions;

/**
 * Exception cause by wrong Code!
 * 
 * @author Lucas Gonçalves
 * 
 */
public class TesterException extends ArangiTesterException {

	private static final long	serialVersionUID	= 1L;

	public TesterException(String msg) {
		super(msg);
	}

	public TesterException(String msg, Throwable e) {
		super(msg, e);
	}
}
