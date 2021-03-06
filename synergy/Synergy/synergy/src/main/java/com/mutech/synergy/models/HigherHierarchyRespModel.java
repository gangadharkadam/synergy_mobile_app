package com.mutech.synergy.models;

import java.util.ArrayList;

public class HigherHierarchyRespModel {

	private ArrayList<HHSubModel> message;
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<HHSubModel> getMessage() {
		return message;
	}
	public void setMessage(ArrayList<HHSubModel> message) {
		this.message = message;
	}

	public class HHSubModel{
		private String zone;
		private String senior_cell;
		private String church_group;
		private String region;
		private String church;
		private String pcf;
		private String name;
		private String group_church_name;
		private String cell;
		private String cell_name;
		private String region_name;
		private String zone_name;
		private String church_name;
		private String pcf_name;
		private String senior_cell_name;

		public String getZone() {
			return zone;
		}
		public void setZone(String zone) {
			this.zone = zone;
		}
		public String getSenior_cell() {
			return senior_cell;
		}
		public void setSenior_cell(String senior_cell) {
			this.senior_cell = senior_cell;
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
		public String getChurch() {
			return church;
		}
		public void setChurch(String church) {
			this.church = church;
		}
		public String getPcf() {
			return pcf;
		}
		public void setPcf(String pcf) {
			this.pcf = pcf;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}


		public String getGroup_church_name() {
			return group_church_name;
		}

		public void setGroup_church_name(String group_church_name) {
			this.group_church_name = group_church_name;
		}

		public String getCell() {
			return cell;
		}

		public void setCell(String cell) {
			this.cell = cell;
		}

		public String getCell_name() {
			return cell_name;
		}

		public void setCell_name(String cell_name) {
			this.cell_name = cell_name;
		}

		public String getRegion_name() {
			return region_name;
		}

		public void setRegion_name(String region_name) {
			this.region_name = region_name;
		}

		public String getZone_name() {
			return zone_name;
		}

		public void setZone_name(String zone_name) {
			this.zone_name = zone_name;
		}

		public String getChurch_name() {
			return church_name;
		}

		public void setChurch_name(String church_name) {
			this.church_name = church_name;
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
	}



}
