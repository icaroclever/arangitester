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

public class TestCase {
	/*
	 * List of the informations about the test case
	 */
	private List<Object>	logs	= new ArrayList<Object>();

	/*
	 * This variable contain the test case name
	 */
	private String			testcase;
	private String			javaMethod;						// Defines test case's method name
	/*
	 * This variable contain the test case's start time
	 */
	private Date			startTime;
	/*
	 * This variable contain the test case's end time
	 */
	private Date			endTime;

	private boolean			skip;

	/**
	 * Get the test case name
	 * 
	 * @return testcase
	 */
	public String getTestcase() {
		return testcase;
	}

	/**
	 * Set the test case name
	 * 
	 * @param testcase
	 */
	public void setTestcase(String testcase) {
		this.testcase = testcase;
	}

	/**
	 * Get the test case's start time
	 * 
	 * @return startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Set the test case's start time
	 * 
	 * @param startTime
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * Get the test case's end time
	 * 
	 * @return endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Set the test case's end time
	 * 
	 * @param endTime
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Get all informations about this test case
	 * 
	 * @return infos
	 */
	public List<Object> getlogs() {
		return logs;
	}

	public void addLog(Object log) {
		logs.add(log);
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String getJavaMethod() {
		return javaMethod;
	}

	public void setJavaMethod(String javaMethod) {
		this.javaMethod = javaMethod;
	}

}
