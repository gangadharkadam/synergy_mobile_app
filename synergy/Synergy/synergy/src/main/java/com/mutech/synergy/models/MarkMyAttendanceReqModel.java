package com.mutech.synergy.models;

import java.util.ArrayList;

public class MarkMyAttendanceReqModel {

	private String username;
	private String userpass;
	private String email,sms,push;
	private ArrayList<RecordModel> records;
//	private ArrayList<RecordModel> records;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpass() {
		return userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String Email) {
		this.email = Email;
	}
	
	
	public String getSms() {
		return sms;
	}

	public void setSms(String Sms) {
		this.sms = Sms;
	}
	
	public String getPush() {
		return userpass;
	}

	public void setPush(String Push) {
		this.push = Push;
	}
	
	
	
	public ArrayList<RecordModel> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<RecordModel> records) {
		this.records = records;
	}	

	public ArrayList<RecordModel> getRecord() {
		return records;
	}

	public void setRecord(ArrayList<RecordModel> record) {
		this.records = record;
	}



	public class RecordModel{
		private String name;
		private String present;
		private String comments;
		private String person_name;

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPresent() {
			return present;
		}
		public void setPresent(String present) {
			this.present = present;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public String getPerson_name() {
			return person_name;
		}
		public void setPerson_name(String person_name) {
			this.person_name = person_name;
		}


	}


}
