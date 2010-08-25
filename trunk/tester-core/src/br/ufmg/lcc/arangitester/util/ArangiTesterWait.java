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
package br.ufmg.lcc.arangitester.util;

/**
 * Adapted from com.thoughtworks.selenium.Wait.
 * 
 * A utility class, designed to help the user automatically wait until a
 * condition turns true.
 * 
 * Use it like this:
 * 
 * 
 * <p>
 * <code>
 * //Checks if the close button is present in the page.<br/> 
 * //Check its existence for 5 seconds<br/>
 * boolean isPresent = new LccWait(5000) {<br/> 
 * &nbsp;&nbsp;&nbsp;&nbsp;boolean until() {<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;return selenium.isElementPresent("button_Close");<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
 * }.getCondition();<br/>
 * </code>
 * </p>
 * 
 */
public abstract class ArangiTesterWait {

	/** Returns true when it's time to stop waiting */
	public abstract boolean until();

	/** The amount of time to wait before giving up; the default is 30 seconds */
	public static final long	DEFAULT_TIMEOUT		= 30000l;

	/** The interval to pause between checking; the default is 500 milliseconds */
	public static final long	DEFAULT_INTERVAL	= 500l;
	/**
	 * Saves the state of the condition implemented by the method until().
	 */
	private boolean				condition			= false;

	/**
	 * Wait until the "until" condition returns true or time runs out.<br/>
	 * Changes the state of the object according to the result of until().
	 * Use the getCondition() method to see what happened.
	 * 
	 * @param timeoutInMilliseconds
	 *            the amount of time to wait before giving up
	 * @see #until()
	 */
	public ArangiTesterWait(long timeoutInMilliseconds) {
		waitReturning(timeoutInMilliseconds, DEFAULT_INTERVAL);
	}

	/**
	 * Wait until the "until" condition returns true or time runs out.
	 * 
	 * @param timeoutInMilliseconds
	 *            the amount of time to wait before giving up
	 * @param intervalInMilliseconds
	 *            the interval to pause between checking "until"
	 * @return False if "until" doesn't return true until the timeout
	 * @see #until()
	 */
	public void waitReturning(long timeoutInMilliseconds, long intervalInMilliseconds) {
		long start = System.currentTimeMillis();
		long end = start + timeoutInMilliseconds;
		while (System.currentTimeMillis() < end) {
			if (until()) {
				condition = true;
				return;
			}
			try {
				Thread.sleep(intervalInMilliseconds);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		condition = false;
	}

	/**
	 * Check if the if time ran out or the condition was successful.
	 * 
	 * @return True if the condition returned true or false if time runs out.
	 */
	public boolean getCondition() {
		return this.condition;
	}

}
