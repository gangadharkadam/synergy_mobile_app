package com.mutech.synergy.models;

import java.util.ArrayList;

public class EventModel {

	private String status;
	private ArrayList<EventSubModel> message;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<EventSubModel> getMessage() {
		return message;
	}
	public void setMessage(ArrayList<EventSubModel> message) {
		this.message = message;
	}

	public class EventSubModel{

		private String event_name;
		private String event_date;
		private String subject;
		private String address;

		public String getEvent_name() {
			return event_name;
		}
		public void setEvent_name(String event_name) {
			this.event_name = event_name;
		}
		public String getEvent_date() {
			return event_date;
		}
		public void setEvent_date(String event_date) {
			this.event_date = event_date;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
	}

}
