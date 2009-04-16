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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lukasmeirelles
 *
 */
public class UseCase {
	private String name;				// Defines use case's name
	private List<TestCase> testcases = new ArrayList<TestCase>();	// This list will contain all test cases
	private Date startTime;
	private Date endTime;
	private List<Object> logs = new ArrayList<Object>();
	private List<String> obs = new ArrayList<String>();
	
	public List<String> getObs() {
		return obs;
	}

	public void setObs(List<String> obs) {
		this.obs = obs;
	}

	/**
	 * Get the use case name
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the use case name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get informations about a specific use case
	 * @return the testcases
	 */
	public List<TestCase> getTestcases() {
		return testcases;
	}
	
	/**
	 * Set a specific use case
	 * @param testcases the testcases to set
	 */
	public void setTestcases(List<TestCase> testcases) {
		this.testcases = testcases;
	}
	
	/**
	 * Get the use case start time.
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	
	/**
	 * Set the use case start time.
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * Get the use case end time.
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	
	/**
	 * Set the use case end time.
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Get all informations about this test case
	 * @return infos
	 */
	public List<Object> getlogs() {
		return logs;
	}
	
	public void addLog(Object log){
		logs.add(log);
	}
}
