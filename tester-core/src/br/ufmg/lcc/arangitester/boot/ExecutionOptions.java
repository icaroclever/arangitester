package br.ufmg.lcc.arangitester.boot;

import java.lang.reflect.Method;

import org.apache.log4j.Level;

import br.ufmg.lcc.arangitester.exceptions.TesterException;

/**
 * Extract informations from command args passed to Reactor.
 * @author Lucas Gonçalves
 *
 */
public class ExecutionOptions {
	//private Logger rootLogger = Logger.getRootLogger();
	private String[] args = null;
	
	public ExecutionOptions(String[] args){
		this.args = args;
		
	}
	
	public boolean isToExecuteAllClasses(){
		return args.length == 0;
	}
	
	public Class<?> getClassFromCommand(){
		try {
			return Class.forName(args[0]);
		} catch (ClassNotFoundException e) {
			throw new TesterException("A classe " + args[0] + " não foi encontrada no Classloader");
		}
	}
	
	public Method getMethodFromCommand(){
		if ( args.length < 2 ){
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
	 * @return The Log4j level, an enum. Like Level.INFO or Level.DEBUG
	 */
	public Level getLogLevel(){
		String logLevel;
		for (int i = 0; i < args.length; i++) {
			if(args[i].equalsIgnoreCase(("-l")) && (i+1) < args.length){
				logLevel = args[i+1];
				// Tratar mais casos aqui. All, Warn, Error, etc
				if(logLevel.equalsIgnoreCase("debug")) return Level.DEBUG;
			}
		}
		return Level.INFO; // Padrão
	}
}
