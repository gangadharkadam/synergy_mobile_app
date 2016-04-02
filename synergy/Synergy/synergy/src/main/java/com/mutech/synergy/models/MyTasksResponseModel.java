package com.mutech.synergy.models;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyTasksResponseModel {

	/*
	 * { "message": [ { "status": "Open", "cell": "WNZ/CHR0001/CEL0001",
	 * "exp_end_date": "2015-06-20", "description": "task in project  ",
	 * "priority": "Urgent", "subject": "followup task for TASK00005", "name":
	 * "TASK00008" } ] }
	 */
	@SerializedName("message")
	private ArrayList<Message> taskList;

	public ArrayList<Message> getTaskList() {
		return taskList;
	}

	public void setTaskList(ArrayList<Message> taskList) {
		this.taskList = taskList;
	}

	public class Message {
		@Expose
		private String status;
		@Expose
		private String cell;
		@Expose
		private String exp_end_date;
		@Expose
		private String description;
		@Expose
		private String priority;

		private String _assign;
		private String assignee;
		private String followup_task;
		private String pcf;
		private String senior_cell;
		
		private String username;
		private String userpass;

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

		public String getExp_end_date() {
			return exp_end_date;
		}

		public void setExp_end_date(String exp_end_date) {
			this.exp_end_date = exp_end_date;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		

		public String get_assign() {
			return _assign;
		}

		public void set_assign(String _assign) {
			this._assign = _assign;
		}

		public String getAssignee() {
			return assignee;
		}

		public void setAssignee(String assignee) {
			this.assignee = assignee;
		}

		public String getFollowup_task() {
			return followup_task;
		}

		public void setFollowup_task(String followup_task) {
			this.followup_task = followup_task;
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



		@Expose
		private String subject;
		@Expose
		private String name;
	}

}
