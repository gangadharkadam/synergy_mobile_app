package com.mutech.synergy.models;

import java.util.ArrayList;

public class EventParticipantsModel {

	private String status;
	private ArrayList<EventParticipantSubModel> message;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<EventParticipantSubModel> getMessage() {
		return message;
	}
	public void setMessage(ArrayList<EventParticipantSubModel> message) {
		this.message = message;
	}

	public class EventParticipantSubModel{

		private String member;
		private String member_name;
		private String person_name;
		private String name;
		private int present;
		private String comments;

		//for my events
		private String event_code,to_date;
		private String event_date;
		private String address;
		private String subject;

		
		
		
		public String getMember() {
			return member;
		}
		public void setMember(String member) {
			this.member = member;
		}
		public String getTo_date() {
			return to_date;
		}
		public void setTo_date(String to_date) {
			this.to_date = to_date;
		}
		public String getEvent_code() {
			return event_code;
		}
		public void setEvent_code(String event_code) {
			this.event_code = event_code;
		}
		public String getMember_name() {
			return member_name;
		}
		public void setMember_name(String member_name) {
			this.member_name = member_name;
		}
		public String getPerson_name() {
			return person_name;
		}
		public void setPerson_name(String person_name) {
			this.person_name = person_name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getPresent() {
			return present;
		}
		public void setPresent(int present) {
			this.present = present;
		}
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public String getEvent_date() {
			return event_date;
		}
		public void setEvent_date(String event_date) {
			this.event_date = event_date;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}



	}
}
