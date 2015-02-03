package com.ibm.ipg.game.exception;

public class IpgException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int severityLevel;

	public IpgException(String msg, int severityLevel) {
		super(msg);
		this.severityLevel = severityLevel;
	}

	public int getSeverityLevel() {
		return severityLevel;
	}
}
