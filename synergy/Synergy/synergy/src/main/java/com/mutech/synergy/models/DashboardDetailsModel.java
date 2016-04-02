package com.mutech.synergy.models;

import java.util.ArrayList;

public class DashboardDetailsModel {

	private DashSubjModel message;

	public DashSubjModel getMessage ()
	{
		return message;
	}

	public void setMessage (DashSubjModel message)
	{
		this.message = message;
	}

	public class DashSubjModel{
	
//		private ArrayList<MemStrengthModel> new_converts;
//		private ArrayList<MemStrengthModel>invities_contacts;
//		private ArrayList<MemStrengthModel>first_timers;
		
		
		
		
		private int Week2,Year2,Month2;
		public int getWeek2() {
			return Week2;
		}

		public void setWeek2(int week2) {
			Week2 = week2;
		}

		public int getYear2() {
			return Year2;
		}

		public void setYear2(int year2) {
			Year2 = year2;
		}

		public int getMonth2() {
			return Month2;
		}

		public void setMonth2(int month2) {
			Month2 = month2;
		}

		private int Week3,Year3,Month3;
		
		public int getWeek3() {
			return Week3;
		}

		public void setWeek3(int week3) {
			Week3 = week3;
		}

		public int getYear3() {
			return Year3;
		}

		public void setYear3(int year3) {
			Year3 = year3;
		}

		public int getMonth3() {
			return Month3;
		}

		public void setMonth3(int month3) {
			Month3 = month3;
		}

//		public ArrayList<MemStrengthModel> getNew_converts() {
//			return new_converts;
//		}
//
//		public void setNew_converts(ArrayList<MemStrengthModel> new_converts) {
//			this.new_converts = new_converts;
//		}
//
//		public ArrayList<MemStrengthModel> getInvities_contacts() {
//			return invities_contacts;
//		}
//
//		public void setInvities_contacts(ArrayList<MemStrengthModel> invities_contacts) {
//			this.invities_contacts = invities_contacts;
//		}
//
//		public ArrayList<MemStrengthModel> getFirst_timers() {
//			return first_timers;
//		}
//
//		public void setFirst_timers(ArrayList<MemStrengthModel> first_timers) {
//			this.first_timers = first_timers;
//		}
//		

		private ArrayList<MemStrengthModel> membership_strength;

		
		private ArrayList<DashPartnershipModel> partnership;


		public ArrayList<MemStrengthModel> getMembership_strength() {
			return membership_strength;
		}

		public void setMembership_strength(
				ArrayList<MemStrengthModel> membership_strength) {
			this.membership_strength = membership_strength;
		}

		public ArrayList<DashPartnershipModel> getPartnership() {
			return partnership;
		}

		public void setPartnership(ArrayList<DashPartnershipModel> partnership) {
			this.partnership = partnership;
		}
	}

	public class MemStrengthModel{
		private String month;

		private String total_member_count;

		private String new_converts;

		public String getMonth ()
		{
			return month;
		}

		public void setMonth (String month)
		{
			this.month = month;
		}

		public String getTotal_member_count ()
		{
			return total_member_count;
		}

		public void setTotal_member_count (String total_member_count)
		{
			this.total_member_count = total_member_count;
		}

		public String getNew_converts ()
		{
			return new_converts;
		}

		public void setNew_converts (String new_converts)
		{
			this.new_converts = new_converts;
		}
	}

	public class DashPartnershipModel{
		private int pledge;
		private int giving;
		private String Month;

		public int getPledge() {
			return pledge;
		}

		public void setPledge(int pledge) {
			this.pledge = pledge;
		}

		public int getGiving() {
			return giving;
		}

		public void setGiving(int giving) {
			this.giving = giving;
		}

		public String getMonth (){
			return Month;
		}

		public void setMonth (String Month){
			this.Month = Month;
		}
	}


	
}
