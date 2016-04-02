package com.mutech.synergy.models;

import java.util.ArrayList;

public class AllMessageBoardModel {
	
	private ArrayList<AllMemSubResponseModel> message;
	
	public ArrayList<AllMemSubResponseModel> getMessage() {
		return message;
	}

	public void setMessage(ArrayList<AllMemSubResponseModel> message) {
		this.message = message;
	}

	public class AllMemSubResponseModel{
		private String ftv_name;

		public String getName() {
			return ftv_name;
		}

		public void setName(String ftv_name) {
			this.ftv_name = ftv_name;
		}

		
		

		
	}

}
