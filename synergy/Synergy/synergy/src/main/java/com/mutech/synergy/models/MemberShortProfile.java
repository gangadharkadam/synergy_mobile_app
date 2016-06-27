package com.mutech.synergy.models;

/**
 * Created by sumitmore on 6/27/16.
 */
public class MemberShortProfile {

    String name;
    String email_id;
    String image;
    String member_name;
    String cell;
    String home_address_landmark;
    String date_of_birth;
    String address;
    String member_designation;
    String cell_name;
    String phone_1;
    String role;

    private String username;
    private String userpass;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getHome_address_landmark() {
        return home_address_landmark;
    }

    public void setHome_address_landmark(String home_address_landmark) {
        this.home_address_landmark = home_address_landmark;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMember_designation() {
        return member_designation;
    }

    public void setMember_designation(String member_designation) {
        this.member_designation = member_designation;
    }

    public String getCell_name() {
        return cell_name;
    }

    public void setCell_name(String cell_name) {
        this.cell_name = cell_name;
    }

    public String getPhone_1() {
        return phone_1;
    }

    public void setPhone_1(String phone_1) {
        this.phone_1 = phone_1;
    }
}
