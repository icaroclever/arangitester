package br.ufmg.lcc.arangitester.exceptions;

public class InvokeException extends LccException {

	private static final long serialVersionUID = 1L;
	
	public InvokeException(String msg) {
		super(msg);
	}
	
	public InvokeException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
