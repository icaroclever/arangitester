package br.ufmg.lcc.arangitester.exceptions;

/**
 * Used on verification of fields. If the value of field is not expected to be that
 * then throw this exception
 * @author Lucas Gonçalves
 *
 */
public class WrongValueException extends LccException {

	private static final long serialVersionUID = 1L;
	private String actualValue;
	private String expectedValue;
	
	public WrongValueException(String msg) {
		super(msg);
	}
	
	public WrongValueException(String msg, String expectedValue, String actualValue) {
		super(msg);
		this.expectedValue = expectedValue;
		this.actualValue = actualValue;
	}

	public String getActualValue() {
		return actualValue;
	}

	public String getExpectedValue() {
		return expectedValue;
	}
}
