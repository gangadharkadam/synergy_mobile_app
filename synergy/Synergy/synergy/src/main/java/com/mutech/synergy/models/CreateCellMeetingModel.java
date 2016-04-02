package com.mutech.synergy.models;

public class CreateCellMeetingModel {

	/*"doctype": "Attendance Record",
    "meeting_category": "Cell Meeting",
    "meeting_sub": "Sunday Service (Weekly)",
    "from_date": "2015-05-23 06:22:22",
    "to_date": "2015-05-27 09:22:26",
    "venue": "test venue",
    "cell": "TZONE01/CHR0001/CEL0001",
    "senior_cell": "TZONE01/CHR0001/SCL0001",
    "pcf": "TZONE01/CHR0001/PCF0001",
    "church": "TZONE01/CHR0001",
    "church_group": "TZONE01/GRP0001",
    "zone": "TZONE01",
    "region": "TREG01"
	 */

	private String doctype;
	private String meeting_category;
	private String meeting_sub;
	private String from_date;
	private String to_date;
	private String venue;
	private String cell;
	private String senior_cell;
	private String pcf;
	private String church;
	private String church_group;
	private String zone;
	private String region;
	private String username;
	private String userpass;

	public String getDoctype() {
		return doctype;
	}
	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}
	public String getMeeting_category() {
		return meeting_category;
	}
	public void setMeeting_category(String meeting_category) {
		this.meeting_category = meeting_category;
	}
	public String getMeeting_sub() {
		return meeting_sub;
	}
	public void setMeeting_sub(String meeting_sub) {
		this.meeting_sub = meeting_sub;
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
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
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
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
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
	


}
