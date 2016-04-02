package com.mutech.synergy.models;

import java.util.ArrayList;

public class UserRolesResponseModel {

	private UserRolesRespOneModel message;

	public UserRolesRespOneModel getMessage() {
		return message;
	}

	public void setMessage(UserRolesRespOneModel message) {
		this.message = message;
	}

	public class UserRolesRespOneModel{
		private ArrayList<RolesUserValues> user_values;
		private ArrayList<RolesRolesValues> roles;

		public ArrayList<RolesUserValues> getUser_values() {
			return user_values;
		}
		public void setUser_values(ArrayList<RolesUserValues> user_values) {
			this.user_values = user_values;
		}
		public ArrayList<RolesRolesValues> getRoles() {
			return roles;
		}
		public void setRoles(ArrayList<RolesRolesValues> roles) {
			this.roles = roles;
		}


	}


	public class RolesUserValues{
		private String defvalue;
		private String defkey;

		public String getDefvalue() {
			return defvalue;
		}
		public void setDefvalue(String defvalue) {
			this.defvalue = defvalue;
		}
		public String getDefkey() {
			return defkey;
		}
		public void setDefkey(String defkey) {
			this.defkey = defkey;
		}


	}

	public class RolesRolesValues{
		private String role;

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}


	}

}
