package com.mutech.synergy.models;

import java.util.ArrayList;

public class UserRolesModel {
	
	private String message;
	private ArrayList<UserValues> user_values;
	private ArrayList<Roles> roles;
	
	
	class UserValues{
		private String defvalue;
		private String defkey;
	}
	
	class Roles{
		private String role;
	}

}
