package com.mutech.synergy.models;

import java.util.ArrayList;

public class MeetingModel {

	private ArrayList<MeetingListModel> message;
	private String status;

	public ArrayList<MeetingListModel> getMessage() {
		return message;
	}

	public void setMessage(ArrayList<MeetingListModel> message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public class MeetingListModel{

		private String meeting_subject;
		private String meeting_name;
		private String venue;
		private String meeting_date;
		
		//my meeting added params
		private String meeting_category;
		private String from_date;
		private String to_date;
		private int present;
		private String name,pcf_name,record_name;
		
		public String getRecord_name() {
			return record_name;
		}
		public void setRecord_name(String record_name) {
			this.record_name = record_name;
		}
		
		
		public String getPcf_name() {
			return pcf_name;
		}
		public void setPcf_name(String pcf_name) {
			this.pcf_name = pcf_name;
		}
		public String getMeeting_subject() {
			return meeting_subject;
		}
		public void setMeeting_subject(String meeting_subject) {
			this.meeting_subject = meeting_subject;
		}
		public String getMeeting_name() {
			return meeting_name;
		}
		public void setMeeting_name(String meeting_name) {
			this.meeting_name = meeting_name;
		}
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
		public String getMeeting_category() {
			return meeting_category;
		}
		public void setMeeting_category(String meeting_category) {
			this.meeting_category = meeting_category;
		}
		public String getFrom_date() {
			return from_date;
		}
		public void setFrom_date(String from_date) {
			this.from_date = from_date;
		}
		public String getTo_date() {
			return to_date;
		}
		public void setTo_date(String to_date) {
			this.to_date = to_date;
		}
		public int getPresent() {
			return present;
		}
		public void setPresent(int present) {
			this.present = present;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		

		

		/*	"Meeting Subject": "mobile api",

        "Meeting Name": "SNZ/SNCh/CELL0001/CELLATT0001",

        "venue": "abuja",

        "Meeting Date": "2015-05-22 08:16:24" */


	}

}
