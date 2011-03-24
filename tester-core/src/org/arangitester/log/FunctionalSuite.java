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
package org.arangitester.log;

import java.util.ArrayList;
import java.util.List;

public class FunctionalSuite {
	/*
	 * This list will contain all informations about all of use cases
	 */
	private List<UseCase>	cases	= new ArrayList<UseCase>();

	/**
	 * Get informations about a specific use case
	 * 
	 * @return cases
	 */
	public List<UseCase> getCases() {
		return cases;
	}

	/**
	 * Modify a specific use case
	 * 
	 * @param cases
	 */
	public void setCases(List<UseCase> cases) {
		this.cases = cases;
	}

}
