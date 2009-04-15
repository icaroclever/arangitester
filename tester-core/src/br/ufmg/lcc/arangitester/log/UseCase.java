/**
 * 
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
