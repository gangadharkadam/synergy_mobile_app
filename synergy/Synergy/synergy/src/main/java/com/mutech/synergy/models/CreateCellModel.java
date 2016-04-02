package com.mutech.synergy.models;

public class CreateCellModel {

	/*
	 * data={
	"cell_code": "cell code 1",
	"cell_name": "cell name 1",
	"contact_phone_no": "9960066444",
	"contact_email_id": "gangadhar.k@indictranstech.com",
	"senior_cell": "TZONE01/CHR0001/SCL0002",
	"pcf": "TZONE01/CHR0001/PCF0002",
	"church": "TZONE01/CHR0001",
	"church_group": "TZONE01/GRP0001",
	"region": "TREG01",
	"zone": "TZONE01"
	}
	 * 
	 * */

	private String cell_code;
	private String cell_name;
	private String contact_phone_no;
	private String contact_email_id;
	private String senior_cell;
	private String pcf;
	private String church;
	private String church_group;
	private String region;
	private String zone;
	private String username;
	private String userpass;
	private String meeting_location;
	private String address;

	public String getCell_code() {
		return cell_code;
	}
	public void setCell_code(String cell_code) {
		this.cell_code = cell_code;
	}
	public String getCell_name() {
		return cell_name;
	}
	public void setCell_name(String cell_name) {
		this.cell_name = cell_name;
	}
	public String getContact_phone_no() {
		return contact_phone_no;
	}
	public void setContact_phone_no(String contact_phone_no) {
		this.contact_phone_no = contact_phone_no;
	}
	public String getContact_email_id() {
		return contact_email_id;
	}
	public void setContact_email_id(String contact_email_id) {
		this.contact_email_id = contact_email_id;
	}
	public String getSenior_cell() {
		return senior_cell;
	}
	public void setSenior_cell(String senior_cell) {
		this.senior_cell = senior_cell;
	}
	public String getPcf() {
		return pcf;
	}
	public void setPcf(String pcf) {
		this.pcf = pcf;
	}
	public String getChurch() {
		return church;
	}
	public void setChurch(String church) {
		this.church = church;
	}
	public String getChurch_group() {
		return church_group;
	}
	public void setChurch_group(String church_group) {
		this.church_group = church_group;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
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
	public String getMeeting_location() {
		return meeting_location;
	}
	public void setMeeting_location(String meeting_location) {
		this.meeting_location = meeting_location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}



}
