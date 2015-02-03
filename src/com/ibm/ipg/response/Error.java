package com.ibm.ipg.response;

import com.ibm.ipg.game.exception.IpgException;

public class Error extends Message {
	
	private int severityLevel;
	
	public Error(String text, int severityLevel) {
		super(text);
		this.severityLevel = severityLevel;
	}
	
	public Error(IpgException exception) {
		super(exception.getMessage());
		this.severityLevel = exception.getSeverityLevel();
	}
	
	public int getSeverityLevel(){
		return severityLevel;
	}
}
