package com.mutech.synergy.models;

public class ResponseMessageModel2 {
	
	public RespMessage message;
	
	public class RespMessage{
		private String status;
		private String message;
		
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}

	public RespMessage getMessage() {
		return message;
	}

	public void setMessage(RespMessage message) {
		this.message = message;
	}
	
	

}
