package com.mutech.synergy.models;

import com.google.gson.annotations.SerializedName;

public class SaveTaskRequestModel {

	/*
	 * { "username": "pcfleader1@lws.com", "userpass": "password", "status":
	 * "Open", "cell": "WNZ/CHR0001/CEL0001", "description": "task in project",
	 * "exp_end_date": "2015-06-20", "_assign":
	 * "['gangadhar.k@indictranstech.com']", "priority": "Urgent", "subject":
	 * "task in project", "name": "TASK00002", "assignee": "gangadhar kadam" }
	 */

	@SerializedName("name")
	private String name;

	@SerializedName("assignee")
	private String assignee;
	
	@SerializedName("username")
	private String userName;

	@SerializedName("userpass")
	private String userpass;

	@SerializedName("status")
	private String status;

	@SerializedName("cell")
	private String cell;

	@SerializedName("description")
	private String description;

	@SerializedName("exp_end_date")
	private String exp_end_date;

	@SerializedName("_assign")
	private String _assign;

	@SerializedName("priority")
	private String priority;

	@SerializedName("subject")
	private String subject;
	
	private String pcf;
	private String senior_cell;
	private String followup_task;

	@SerializedName("comment")
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserpass() {
		return userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCell() {
		return cell;
	}

	public void setCell(String cell) {
		this.cell = cell;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExp_end_date() {
		return exp_end_date;
	}

	public void setExp_end_date(String exp_end_date) {
		this.exp_end_date = exp_end_date;
	}

	public String get_assign() {
		return _assign;
	}

	public void set_assign(String _assign) {
		this._assign = _assign;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPcf() {
		return pcf;
	}

	public void setPcf(String pcf) {
		this.pcf = pcf;
	}

	public String getSenior_cell() {
		return senior_cell;
	}

	public void setSenior_cell(String senior_cell) {
		this.senior_cell = senior_cell;
	}

	public String getFollowup_task() {
		return followup_task;
	}

	public void setFollowup_task(String followup_task) {
		this.followup_task = followup_task;
	}
	
	
	
	
}
