package br.ufmg.lcc.arangitester.log;

public class Info {
	
	public Info(String message){
		this.message = message;
	}
	
	public Info() {
		
	}

	/**
	 * This variable contain a information about the test
	 */
	private String message;

	/**
	 * Get the object's info message
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Set the object's info message
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
