package br.ufmg.lcc.arangitester.exceptions;

public class FatalException extends Exception {

	private static final long serialVersionUID = 1L;

	public FatalException(String msg, Throwable e) {
		super(msg, e);
	}

	public FatalException(String msg) {
		super(msg);
	}
}
