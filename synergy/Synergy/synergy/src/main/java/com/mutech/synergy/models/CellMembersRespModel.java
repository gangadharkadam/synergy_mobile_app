package com.mutech.synergy.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CellMembersRespModel {

	@Expose
	private List<MemberMessage> message;;

	public List<MemberMessage> getMessage() {
		return message;
	}

	public void setMessage(List<MemberMessage> message) {
		this.message = message;
	}

	public class MemberMessage {

		@SerializedName("member_name")
		@Expose
		private String memberName;
		private String user_id;
		private String ID;
	
		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		/**
		 * 
		 * @return The memberName
		 */
		public String getMemberName() {
			return memberName;
		}

		/**
		 * 
		 * @param memberName
		 *            The member_name
		 */
		public void setMemberName(String memberName) {
			this.memberName = memberName;
		}

	}
}
