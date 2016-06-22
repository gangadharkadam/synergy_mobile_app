package com.mutech.synergy.models;

public class MeetingListRequestModel {

	private String page_no;
	private String username;
	private String userpass;
	private String meeting_id;
	private String event_id;
	private String tbl;
	private String name;
	private String cell;
	private String filename;
	private String fdata;
	private String email;
	private String sms;
	private String push;
    private String message;
    private String recipents;
    private String pcf_name;
    private String record_name;
    
    private String giving_or_pledge;
	private String is_member;
	private String member;
	private String amount;
	private String ministry_year;
	private String church;
	private String date;
	private String partnership_arms;
	private String currency;
	private Integer conversation_rate;
	
    
    
    
	public String getPage_no() {
		return page_no;
	}

	public void setPage_no(String page_no) {
		this.page_no = page_no;
	}

	public String getGiving_or_pledge() {
		return giving_or_pledge;
	}

	public void setGiving_or_pledge(String giving_or_pledge) {
		this.giving_or_pledge = giving_or_pledge;
	}

	public String getIs_member() {
		return is_member;
	}

	public void setIs_member(String is_member) {
		this.is_member = is_member;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		this.member = member;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMinistry_year() {
		return ministry_year;
	}

	public void setMinistry_year(String ministry_year) {
		this.ministry_year = ministry_year;
	}

	public String getChurch() {
		return church;
	}

	public void setChurch(String church) {
		this.church = church;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPartnership_arms() {
		return partnership_arms;
	}

	public void setPartnership_arms(String partnership_arms) {
		this.partnership_arms = partnership_arms;
	}

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

	public String getRecipents() {
		return recipents;
	}

	public void setRecipents(String recipents) {
		this.recipents = recipents;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getPush() {
		return push;
	}

	public void setPush(String push) {
		this.push = push;
	}

	private String email_id;

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
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

	public String getMeeting_id() {
		return meeting_id;
	}

	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public String getEvent_id() {
		return event_id;
	}

	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}

	public String getTbl() {
		return tbl;
	}

	public void setTbl(String tbl) {
		this.tbl = tbl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFdata() {
		return fdata;
	}

	public void setFdata(String fdata) {
		this.fdata = fdata;
	}


	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getConversation_rate() {
		return conversation_rate;
	}

	public void setConversation_rate(int conversation_rate) {
		this.conversation_rate = conversation_rate;
	}
}
