package com.example.bloodgenix.Models;

public class SearchRecordDatabase {
    String profileImg, personName;
    String location, bloodGroupSearch;

    public SearchRecordDatabase(){
    }

    public SearchRecordDatabase(String profileImg, String personName, String location, String bloodGroupSearch) {
        this.profileImg = profileImg;
        this.personName = personName;
        this.location = location;
        this.bloodGroupSearch = bloodGroupSearch;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBloodGroupSearch() {
        return bloodGroupSearch;
    }

    public void setBloodGroupSearch(String bloodGroupSearch) {
        this.bloodGroupSearch = bloodGroupSearch;
    }
}
