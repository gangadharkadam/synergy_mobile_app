package com.mutech.synergy.models;

import java.util.ArrayList;

import android.sax.StartElementListener;
import android.util.Log;

public class MemberProfileModel {
	
	private ArrayList<ProfileSubModel> message;
	private String status;
	
	public ArrayList<ProfileSubModel> getMessage() {
		return message;
	}

	public void setMessage(ArrayList<ProfileSubModel> message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public class ProfileSubModel{
		
		private String pcf_name;
		private String surname;
		private String senior_cell_name;
		private String cell_name;
		private String church_name;
		private String group_church_name;
		
        private String zone_name; 
		private String region_name;
		
		private String short_bio;
		private String username;
		private String userpass;
		
		private String last_name;
		private String home_address;
		private String office_state;
		private String email_id;
		private String lon;
		private String image;
		private String creation;
		private String member_type;
		private String sex;
		private String church_group;
		private String baptism_when;
		private String church;
		private String owner;
		private String marital_info;
		private String phone_2;
		private String phone_1;
		private String member_name;
		private String cell;
		private String modified_by;
		private String zone;
		private String office_address;
		private String industry_segment;
		private String employment_status;
		private String when;
		private String office_landmark;
		private String age_group;
		private String date_of_birth;
		private String is_eligibale_for_follow_up;
		private String is_new_born;
		private String docstatus;
		private String yokoo_id;
		private String pcf;
		private String is_new_convert;
		private String educational_qualification;
		private String baptism_where;
		private String senior_cell;
		private String yearly_income;
		private String experience_years;
		private String lon1;
		private String flag;
		private String ftv_id_no;
		private String office_city;
		private String address;
		private String lat;
		private String user_id;
		private String date_of_join;
		private String email_id2;
		private String lat1;
		private String name;
		private String idx;
		private String office_address_street;
		private String region;
		private String title;
		private String modified;
		private String parenttype;
		private String education_qualification;
		private String school_status;
		private String baptisum_status;
		private String core_competeance;
		private String member_designation;
		private String filled_with_holy_ghost;
		private String where;
		private String parentfield;
		private String password;
		private String role;
		
		
		
		
		
		public String getSurname() {
			return surname;
		}
		public void setSurname(String surname) {
			this.surname = surname;
		}
		public String getPcf_name() {
			return pcf_name;
		}
		public void setPcf_name(String pcf_name) {
			this.pcf_name = pcf_name;
		}
		public String getSenior_cell_name() {
			return senior_cell_name;
		}
		public void setSenior_cell_name(String senior_cell_name) {
			this.senior_cell_name = senior_cell_name;
		}
		public String getCell_name() {
			return cell_name;
		}
		public void setCell_name(String cell_name) {
			this.cell_name = cell_name;
		}
		public String getChurch_name() {
			return church_name;
		}
		public void setChurch_name(String church_name) {
			this.church_name = church_name;
		}
		public String getGroup_church_name() {
			return group_church_name;
		}
		public void setGroup_church_name(String group_church_name) {
			this.group_church_name = group_church_name;
		}
		public String getZone_name() {
			return zone_name;
		}
		public void setZone_name(String zone_name) {
			this.zone_name = zone_name;
		}
		public String getRegion_name() {
			return region_name;
		}
		public void setRegion_name(String region_name) {
			this.region_name = region_name;
		}
		public String getShort_bio() {
			return short_bio;
		}
		public void setShort_bio(String short_bio) {
			this.short_bio = short_bio;
		}
		public String getLast_name() {
			return last_name;
		}
		public void setLast_name(String last_name) {
			this.last_name = last_name;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getHome_address() {
			return home_address;
		}
		public void setHome_address(String home_address) {
			this.home_address = home_address;
		}
		public String getOffice_state() {
			return office_state;
		}
		public void setOffice_state(String office_state) {
			this.office_state = office_state;
		}
		public String getEmail_id() {
			return email_id;
		}
		public void setEmail_id(String email_id) {
			this.email_id = email_id;
		}
		public String getLon() {
			return lon;
		}
		public void setLon(String lon) {
			this.lon = lon;
		}
		public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public String getCreation() {
			return creation;
		}
		public void setCreation(String creation) {
			this.creation = creation;
		}
		public String getMember_type() {
			return member_type;
		}
		public void setMember_type(String member_type) {
			this.member_type = member_type;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getChurch_group() {
			return church_group;
		}
		public void setChurch_group(String church_group) {
			this.church_group = church_group;
		}
		public String getBaptism_when() {
			return baptism_when;
		}
		public void setBaptism_when(String baptism_when) {
			this.baptism_when = baptism_when;
		}
		public String getChurch() {
			return church;
		}
		public void setChurch(String church) {
			this.church = church;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getMarital_info() {
			return marital_info;
		}
		public void setMarital_info(String marital_info) {
			this.marital_info = marital_info;
		}
		public String getPhone_2() {
			return phone_2;
		}
		public void setPhone_2(String phone_2) {
			this.phone_2 = phone_2;
		}
		public String getPhone_1() {
			return phone_1;
		}
		public void setPhone_1(String phone_1) {
			this.phone_1 = phone_1;
		}
		public String getMember_name() {
			return member_name;
		}
		public void setMember_name(String member_name) {
			this.member_name = member_name;
		}
		public String getCell() {
			return cell;
		}
		public void setCell(String cell) {
			this.cell = cell;
		}
		public String getModified_by() {
			return modified_by;
		}
		public void setModified_by(String modified_by) {
			this.modified_by = modified_by;
		}
		public String getZone() {
			return zone;
		}
		public void setZone(String zone) {
			this.zone = zone;
		}
		public String getOffice_address() {
			return office_address;
		}
		public void setOffice_address(String office_address) {
			this.office_address = office_address;
		}
		public String getIndustry_segment() {
			return industry_segment;
		}
		public void setIndustry_segment(String industry_segment) {
			this.industry_segment = industry_segment;
		}
		public String getEmployment_status() {
			return employment_status;
		}
		public void setEmployment_status(String employment_status) {
			this.employment_status = employment_status;
		}
		public String getWhen() {
			return when;
		}
		public void setWhen(String when) {
			this.when = when;
		}
		public String getOffice_landmark() {
			return office_landmark;
		}
		public void setOffice_landmark(String office_landmark) {
			this.office_landmark = office_landmark;
		}
		public String getAge_group() {
			return age_group;
		}
		public void setAge_group(String age_group) {
			this.age_group = age_group;
		}
		public String getDate_of_birth() {
			return date_of_birth;
		}
		public void setDate_of_birth(String date_of_birth) {
			this.date_of_birth = date_of_birth;
		}
		public String getIs_eligibale_for_follow_up() {
			return is_eligibale_for_follow_up;
		}
		public void setIs_eligibale_for_follow_up(String is_eligibale_for_follow_up) {
			this.is_eligibale_for_follow_up = is_eligibale_for_follow_up;
		}
		public String getIs_new_born() {
			return is_new_born;
		}
		public void setIs_new_born(String is_new_born) {
			this.is_new_born = is_new_born;
		}
		public String getDocstatus() {
			return docstatus;
		}
		public void setDocstatus(String docstatus) {
			this.docstatus = docstatus;
		}
		public String getYokoo_id() {
			return yokoo_id;
		}
		public void setYokoo_id(String yokoo_id) {
			this.yokoo_id = yokoo_id;
		}
		public String getPcf() {
			return pcf;
		}
		public void setPcf(String pcf) {
			this.pcf = pcf;
		}
		public String getIs_new_convert() {
			return is_new_convert;
		}
		public void setIs_new_convert(String is_new_convert) {
			this.is_new_convert = is_new_convert;
		}
		public String getEducational_qualification() {
			return educational_qualification;
		}
		public void setEducational_qualification(String educational_qualification) {
			this.educational_qualification = educational_qualification;
		}
		public String getBaptism_where() {
			return baptism_where;
		}
		public void setBaptism_where(String baptism_where) {
			this.baptism_where = baptism_where;
		}
		public String getSenior_cell() {
			return senior_cell;
		}
		public void setSenior_cell(String senior_cell) {
			this.senior_cell = senior_cell;
		}
		public String getYearly_income() {
			return yearly_income;
		}
		public void setYearly_income(String yearly_income) {
			this.yearly_income = yearly_income;
		}
		public String getExperience_years() {
			return experience_years;
		}
		public void setExperience_years(String experience_years) {
			this.experience_years = experience_years;
		}
		public String getLon1() {
			return lon1;
		}
		public void setLon1(String lon1) {
			this.lon1 = lon1;
		}
		public String getFlag() {
			return flag;
		}
		public void setFlag(String flag) {
			this.flag = flag;
		}
		public String getFtv_id_no() {
			return ftv_id_no;
		}
		public void setFtv_id_no(String ftv_id_no) {
			this.ftv_id_no = ftv_id_no;
		}
		public String getOffice_city() {
			return office_city;
		}
		public void setOffice_city(String office_city) {
			this.office_city = office_city;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getLat() {
			return lat;
		}
		public void setLat(String lat) {
			this.lat = lat;
		}
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getDate_of_join() {
			return date_of_join;
		}
		public void setDate_of_join(String date_of_join) {
			this.date_of_join = date_of_join;
		}
		public String getEmail_id2() {
			return email_id2;
		}
		public void setEmail_id2(String email_id2) {
			this.email_id2 = email_id2;
		}
		public String getLat1() {
			return lat1;
		}
		public void setLat1(String lat1) {
			this.lat1 = lat1;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIdx() {
			return idx;
		}
		public void setIdx(String idx) {
			this.idx = idx;
		}
		public String getOffice_address_street() {
			return office_address_street;
		}
		public void setOffice_address_street(String office_address_street) {
			this.office_address_street = office_address_street;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getModified() {
			return modified;
		}
		public void setModified(String modified) {
			this.modified = modified;
		}
		public String getParenttype() {
			return parenttype;
		}
		public void setParenttype(String parenttype) {
			this.parenttype = parenttype;
		}
		public String getEducation_qualification() {
			return education_qualification;
		}
		public void setEducation_qualification(String education_qualification) {
			this.education_qualification = education_qualification;
		}
		public String getSchool_status() {
			return school_status;
		}
		public void setSchool_status(String school_status) {
			this.school_status = school_status;
		}
		public String getBaptisum_status() {
			Log.d("NonStop", "BAPSTATUS: " + baptisum_status);
			return baptisum_status;
		}
		public void setBaptisum_status(String baptisum_status) {
			this.baptisum_status = baptisum_status;
		}
		public String getCore_competeance() {
			return core_competeance;
		}
		public void setCore_competeance(String core_competeance) {
			this.core_competeance = core_competeance;
		}
		public String getMember_designation() {
			return member_designation;
		}
		public void setMember_designation(String member_designation) {
			this.member_designation = member_designation;
		}
		public String getFilled_with_holy_ghost() {
			return filled_with_holy_ghost;
		}
		public void setFilled_with_holy_ghost(String filled_with_holy_ghost) {
			this.filled_with_holy_ghost = filled_with_holy_ghost;
		}
		public String getWhere() {
			return where;
		}
		public void setWhere(String where) {
			this.where = where;
		}
		public String getParentfield() {
			return parentfield;
		}
		public void setParentfield(String parentfield) {
			this.parentfield = parentfield;
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

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
	}
		

}
