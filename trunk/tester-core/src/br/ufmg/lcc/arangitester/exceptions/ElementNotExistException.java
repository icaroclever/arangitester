package br.ufmg.lcc.arangitester.exceptions;

/**
 * If element is not present on page
 * @author Lucas Gonçalves
 *
 */
public class ElementNotExistException extends LccException{

	private static final long serialVersionUID = 1L;

	public ElementNotExistException(String msg){
		super(msg);
	}
	
	public ElementNotExistException(String msg, Throwable e){
		super(msg, e);
	}
}
