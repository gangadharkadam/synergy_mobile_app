package com.mutech.synergy.models;

public class CreateEventModel {

	private String username;
	private String  userpass;
	private String  subject;
	private String  type;
	private String  starts_on;
	private String  ends_on;
	private String  address;
	private String  description;
	private String event_group;
	private String value;
	
	
	
	public String getEvent_group() {
		return event_group;
	}
	public void setEvent_group(String event_group) {
		this.event_group = event_group;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStarts_on() {
		return starts_on;
	}
	public void setStarts_on(String starts_on) {
		this.starts_on = starts_on;
	}
	public String getEnds_on() {
		return ends_on;
	}
	public void setEnds_on(String ends_on) {
		this.ends_on = ends_on;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	

}
