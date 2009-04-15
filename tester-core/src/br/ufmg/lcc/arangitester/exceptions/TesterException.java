package br.ufmg.lcc.arangitester.exceptions;

/**
 * Exception cause by wrong Code!
 * @author Lucas Gonçalves
 *
 */
public class TesterException extends LccException {

	private static final long serialVersionUID = 1L;

	public TesterException(String msg){
		super(msg);
	}
	
	public TesterException(String msg, Throwable e){
		super(msg, e);
	}
}
