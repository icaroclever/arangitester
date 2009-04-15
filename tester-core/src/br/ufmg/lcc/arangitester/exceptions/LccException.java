package br.ufmg.lcc.arangitester.exceptions;

public class LccException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LccException(String msg) {
		super(msg);
		//LccContext.getInstance().getSeleniumController().getSeleniumClient().captureScreenshot("C:\\screen.png");
	}

	public LccException(String msg, Throwable e) {
		super(msg, e);
	}
}
