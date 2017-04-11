package com.langoor.app.blueshak.services.model;

import java.io.Serializable;

public class NotificationModel implements Serializable {

	
	private String message ;
	private String time;

	public NotificationModel(String message, String time){
		this.message=message;
		this.time=time;

	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
