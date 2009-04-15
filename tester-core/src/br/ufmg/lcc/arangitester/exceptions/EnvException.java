package br.ufmg.lcc.arangitester.exceptions;

/**
 * All environment problem must be wrapped by this exception.
 * 
 * @author Lucas Gonçalves
 * 
 */
public class EnvException extends LccException {
	private static final long serialVersionUID = 1L;

	public EnvException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public EnvException(String msg) {
		super(msg);
	}
}
