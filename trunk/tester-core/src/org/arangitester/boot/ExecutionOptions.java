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
package org.arangitester.boot;

import java.lang.reflect.Method;

import org.apache.log4j.Level;
import org.arangitester.exceptions.EnvException;
import org.arangitester.exceptions.TesterException;


/**
 * Extract informations of parameters passed to ArangiTester main class.
 * 
 * @author Lucas Gonçalves
 * @author Ícaro C. F. Braga
 * 
 */
public class ExecutionOptions {
	
	private String[] parameters	= null;
	
	public static final int CLASS_INDEX = 0;
	public static final int METHOD_INDEX = 1;

	/**
	 * 
	 * @param parameters <br>
	 * There are three ways to pass <b>parameters</b>:
	 * <ol>
	 * <li>Empty</li>
	 * <li>Absolute class name - <i>Eg.: com.test.UserTestSuite</i></li>
	 * <li>Absolute class name as the first parameter and method name as the second - <i>Eg.: com.test.UserTestSuite method</i></li>
	 * </ol>
	 */
	public ExecutionOptions(String[] parameters) {
		this.parameters = parameters;
	}

	/**
	 * Return true if parameters are empty.
	 * @return true if parameters are empty.
	 */
	public boolean isToExecuteAllClasses() {
		return this.parameters.length == 0;
	}

	/**
	 * Return a generic class informed as parameter. 
	 * @return Generic class informed as a parameter
	 */
	public Class<?> getClassFromCommand() {
		if(this.parameters[CLASS_INDEX].length()==0)
			throw new TesterException("The class parameter is empty.");
		try {
			return Class.forName(this.parameters[0]);
		} catch (ClassNotFoundException e) {
			throw new TesterException(String.format("The class '%s' cannot be found in Classloader.",this.parameters[CLASS_INDEX]));
		}
	}

	/**
	 * Return a specific method of a class informed as parameter. 
	 * @return specific method of the class
	 */
	public Method getMethodFromCommand() {
		Class<?> testClass = getClassFromCommand(); 
		if (parameters.length < 2) {
			throw new TesterException("The method parameter is empty.");
		}
		try {
			return testClass.getMethod(this.parameters[METHOD_INDEX]);
		} catch (Exception e) {
			throw new TesterException(String.format("Method '%s' cannot be found in the class '%s'",this.parameters[METHOD_INDEX],this.parameters[CLASS_INDEX]));
		}
	}

	/**
	 * Gets the log4j Log Level passed as parameter in the command line, using -l level.
	 * Like -l debug, or -l error.
	 * @return The Log4j level, an enumeration. The default value of the level is Level.INFO
	 */
	public Level getLogLevel() {
		String logLevel;
		for (int i = 0; i < this.parameters.length; i++) {
			if (this.parameters[i].equalsIgnoreCase(("-l")) && (i + 1) < this.parameters.length) {
				logLevel = this.parameters[i + 1];
				if (logLevel.equalsIgnoreCase("debug")) 		return Level.DEBUG;
				else if (logLevel.equalsIgnoreCase("warn")) 	return Level.WARN;
				else if (logLevel.equalsIgnoreCase("info")) 	return Level.INFO;
				else if (logLevel.equalsIgnoreCase("fatal")) 	return Level.FATAL;
				else if (logLevel.equalsIgnoreCase("trace")) 	return Level.TRACE;
				else if (logLevel.equalsIgnoreCase("off")) 		return Level.OFF;
				else if (logLevel.equalsIgnoreCase("all")) 		return Level.ALL;
				else if (logLevel.equalsIgnoreCase("error")) 	return Level.ERROR;
				else throw new EnvException(String.format("The log field '%s' is not a valid log level.", logLevel));
			}
		}
		return Level.INFO; // default level
	}
}
