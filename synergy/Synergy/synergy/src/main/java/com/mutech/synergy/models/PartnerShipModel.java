package com.mutech.synergy.models;

import java.util.ArrayList;


public class PartnerShipModel {
	
	private ArrayList<PartnerModel> message;
	private String status;
	
	public ArrayList<PartnerModel> getMessage() {
		return message;
	}

	public void setMessage(ArrayList<PartnerModel> message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public class PartnerModel{
		private String partnership_arms,ministry_year,date,amount,giving_or_pledge,member,is_member,partner_name,type_of_pledge,currency,conversation_rate;
		
		
		public String getType_of_pledge() {
			return type_of_pledge;
		}
		public void setType_of_pledge(String type_of_pledge) {
			this.type_of_pledge = type_of_pledge;
		}
		public String getPartner_name() {
			return partner_name;
		}
		public void setPartner_name(String partner_name) {
			this.partner_name = partner_name;
		}
		public String getIs_member() {
			return is_member;
		}
		public void setIs_member(String is_member) {
			this.is_member = is_member;
		}
		public String getpartnership_arms() {
			return partnership_arms;
		}
		public void setpartnership_arms(String partnership_arms) {
			this.partnership_arms = partnership_arms;
		}
		
		public String getministry_year() {
			return ministry_year;
		}
		public void setministry_year(String ministry_year) {
			this.ministry_year = ministry_year;
		}
		
		public String getdate() {
			return date;
		}
		public void setdate(String date) {
			this.date = date;
		}
		
		public String getamount() {
			return amount;
		}
		public void setamount(String amount) {
			this.amount = amount;
		}

		public String getcurrency() {
			return currency;
		}

		public void setcurrency(String currency) {
			this.currency = currency;
		}

		public String getConversation_rate() {
			return conversation_rate;
		}

		public void setConversation_rate(String conversation_rate) {
			this.conversation_rate = conversation_rate;
		}
		
		public String getgiving_or_pledge() {
			return giving_or_pledge;
		}
		public void setgiving_or_pledge(String giving_or_pledge) {
			this.giving_or_pledge = giving_or_pledge;
		}
		public String getMember() {
			return member;
		}
		public void setMember(String member) {
			this.member = member;
		}
		
		
	}

	

}
