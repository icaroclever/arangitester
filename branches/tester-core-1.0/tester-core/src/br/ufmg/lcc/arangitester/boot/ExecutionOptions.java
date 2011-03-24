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
package br.ufmg.lcc.arangitester.boot;

import java.lang.reflect.Method;

import org.apache.log4j.Level;

import br.ufmg.lcc.arangitester.exceptions.TesterException;

/**
 * Extract informations from command args passed to Reactor.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class ExecutionOptions {
	// private Logger rootLogger = Logger.getRootLogger();
	private String[]	args	= null;

	public ExecutionOptions(String[] args) {
		this.args = args;

	}

	public boolean isToExecuteAllClasses() {
		return args.length == 0;
	}

	public Class<?> getClassFromCommand() {
		try {
			return Class.forName(args[0]);
		} catch (ClassNotFoundException e) {
			throw new TesterException("A classe " + args[0] + " não foi encontrada no Classloader");
		}
	}

	public Method getMethodFromCommand() {
		if (args.length < 2) {
			return null;
		}
		try {
			return getClassFromCommand().getMethod(args[1]);
		} catch (Exception e) {
			throw new TesterException("A classe " + args[0] + " não contêm o método " + args[1]);
		}
	}

	/**
	 * Gets the log4j Log Level passed as param in the command line, using -l level.
	 * Like -l debug, or -l error.
	 * Simple method, just to make things work soon ;-)
	 * 
	 * @return The Log4j level, an enum. Like Level.INFO or Level.DEBUG
	 */
	public Level getLogLevel() {
		String logLevel;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase(("-l")) && (i + 1) < args.length) {
				logLevel = args[i + 1];
				// Tratar mais casos aqui. All, Warn, Error, etc
				if (logLevel.equalsIgnoreCase("debug")) return Level.DEBUG;
			}
		}
		return Level.INFO; // Padrão
	}
}
