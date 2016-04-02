package com.mutech.synergy.models;

import java.util.ArrayList;
import java.util.List;

public class SearchResultModel {

	private List<SearchResultSubModel> message = new ArrayList<SearchResultSubModel>();

	public List<SearchResultSubModel> getMessage() {
		return message;
	}

	public void setMessage(List<SearchResultSubModel> message) {
		this.message = message;
	}

	public class SearchResultSubModel{

		//common in all
		private String member_name;//in member & leader
		private String church;//in all

		//member
		private String zone;
		private String email_id;
		private String region;
		private String church_group;
		private String phone_1;

		//leader
		private String phone_no;
		private String group_type;

		//group
		private String senior_cell;
		private String cell;
		private String pcf;

		public String getZone() {
			return zone;
		}
		public void setZone(String zone) {
			this.zone = zone;
		}
		public String getEmail_id() {
			return email_id;
		}
		public void setEmail_id(String email_id) {
			this.email_id = email_id;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getMember_name() {
			return member_name;
		}
		public void setMember_name(String member_name) {
			this.member_name = member_name;
		}
		public String getChurch_group() {
			return church_group;
		}
		public void setChurch_group(String church_group) {
			this.church_group = church_group;
		}
		public String getChurch() {
			return church;
		}
		public void setChurch(String church) {
			this.church = church;
		}
		public String getPhone_1() {
			return phone_1;
		}
		public void setPhone_1(String phone_1) {
			this.phone_1 = phone_1;
		}
		public String getPhone_no() {
			return phone_no;
		}
		public void setPhone_no(String phone_no) {
			this.phone_no = phone_no;
		}
		public String getGroup_type() {
			return group_type;
		}
		public void setGroup_type(String group_type) {
			this.group_type = group_type;
		}
		public String getSenior_cell() {
			return senior_cell;
		}
		public void setSenior_cell(String senior_cell) {
			this.senior_cell = senior_cell;
		}
		public String getCell() {
			return cell;
		}
		public void setCell(String cell) {
			this.cell = cell;
		}
		public String getPcf() {
			return pcf;
		}
		public void setPcf(String pcf) {
			this.pcf = pcf;
		}



	}


}
