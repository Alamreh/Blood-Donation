package com.example.blooddonation.Model;

public class User {
    String name,PhoneNumber,bloodGroup,email,type,profilePictureurl,search,id;

    public User(){

    }
    public User(String name, String phoneNumber, String bloodGroup, String email, String type, String profilePictureurl, String search, String id) {
        this.name = name;
        PhoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
        this.email = email;
        this.type = type;
        this.profilePictureurl = profilePictureurl;
        this.search = search;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfilePictureurl() {
        return profilePictureurl;
    }

    public void setProfilePictureurl(String profilePictureurl) {
        this.profilePictureurl = profilePictureurl;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
