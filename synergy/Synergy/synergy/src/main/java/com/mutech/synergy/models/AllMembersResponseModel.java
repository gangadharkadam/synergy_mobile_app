package com.mutech.synergy.models;

import java.util.ArrayList;

public class AllMembersResponseModel {
	
	private ArrayList<AllMemSubResponseModel> message;
	
	public ArrayList<AllMemSubResponseModel> getMessage() {
		return message;
	}

	public void setMessage(ArrayList<AllMemSubResponseModel> message) {
		this.message = message;
	}

	public class AllMemSubResponseModel{
		private String name,partnership_arms,amount,ftv_name,email_id,record_name;
		
		public String getRecord_name() {
			return record_name;
		}

		public void setRecord_name(String record_name) {
			this.record_name = record_name;
		}

		public String getEmail_id() {
			return email_id;
		}

		public void setEmail_id(String email_id) {
			this.email_id = email_id;
		}

		boolean val=false;

		
		public boolean isVal() {
			return val;
		}

		public void setVal(boolean val) {
			this.val = val;
		}

		public String getFtv_name() {
			return ftv_name;
		}

		public void setFtv_name(String ftv_name) {
			this.ftv_name = ftv_name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public String getPartnership_arms() {
			return partnership_arms;
		}

		public void setPartnership_arms(String partnership_arms) {
			this.partnership_arms = partnership_arms;
		}
		
		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}
		
	}

}
