package com.mutech.synergy.models;

import java.util.ArrayList;

public class MemberDetailsRespModel {
	
	private ArrayList<MemberModel> message;
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<MemberModel> getMessage() {
		return message;
	}

	public void setMessage(ArrayList<MemberModel> message) {
		this.message = message;
	}

	public class MemberModel{
		private String member;
		private String name;
		private String present;
		private String member_name;
		private String Email;
		private String Sms;
		private String Push;
		private String meeting_subject;
		private String venue;
		public String getVenue() {
			return venue;
		}
		public void setVenue(String venue) {
			this.venue = venue;
		}
		public String getMeeting_date() {
			return meeting_date;
		}
		public void setMeeting_date(String meeting_date) {
			this.meeting_date = meeting_date;
		}
		private String meeting_date;
		
		
		public String getMeeting_subject() {
			return meeting_subject;
		}
		public void setMeeting_subject(String meeting_subject) {
			this.meeting_subject = meeting_subject;
		}
		public String getMember() {
			return member;
		}
		public void setMember(String member) {
			this.member = member;
		}
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
		public String getMember_name() {
			return member_name;
		}
		public void setMember_name(String member_name) {
			this.member_name = member_name;
		}	
		
		
		public String getEmail() {
			return Email;
		}
		public void setEmail(String Email) {
			this.Email = Email;
		}	
		
		public String getSms() {
			return Sms;
		}
		public void setSms(String Sms) {
			this.Sms = Sms;
		}	
		
		public String getPush() {
			return Push;
		}
		public void setPush(String Push) {
			this.Push = Push;
		}	
		
	}

}
