package com.example.bloodgenix.Models;

public class DonationDetails {

    String uid;
    public String phoneNumber;
    public String blGroup;
    String ruDiabetic, diabeticCount, otherDisease;
    String ruDonated, donateMonth, weight;
    public String donorLocation;
    public String personFullName;
    public String personProfileImg;

    public DonationDetails(){

    }

    public DonationDetails(String uid, String phoneNumber, String blGroup, String ruDiabetic, String diabeticCount, String otherDisease, String ruDonated, String donateMonth, String weight, String donorLocation, String personFullName, String personProfileImg) {
        this.uid = uid;
        this.phoneNumber = phoneNumber;
        this.blGroup = blGroup;
        this.ruDiabetic = ruDiabetic;
        this.diabeticCount = diabeticCount;
        this.otherDisease = otherDisease;
        this.ruDonated = ruDonated;
        this.donateMonth = donateMonth;
        this.weight = weight;
        this.donorLocation = donorLocation;
        this.personFullName = personFullName;
        this.personProfileImg = personProfileImg;
    }

    public String getPersonProfileImg() {
        return personProfileImg;
    }

    public void setPersonProfileImg(String personProfileImg) {
        this.personProfileImg = personProfileImg;
    }

    public String getPersonFullName() {
        return personFullName;
    }

    public void setPersonFullName(String personFullName) {
        this.personFullName = personFullName;
    }

    public String getDonorLocation() {
        return donorLocation;
    }

    public void setDonorLocation(String donorLocation) {
        this.donorLocation = donorLocation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBlGroup() {
        return blGroup;
    }

    public void setBlGroup(String blGroup) {
        this.blGroup = blGroup;
    }

    public String getRuDiabetic() {
        return ruDiabetic;
    }

    public void setRuDiabetic(String ruDiabetic) {
        this.ruDiabetic = ruDiabetic;
    }

    public String getDiabeticCount() {
        return diabeticCount;
    }

    public void setDiabeticCount(String diabeticCount) {
        this.diabeticCount = diabeticCount;
    }

    public String getOtherDisease() {
        return otherDisease;
    }

    public void setOtherDisease(String otherDisease) {
        this.otherDisease = otherDisease;
    }

    public String getRuDonated() {
        return ruDonated;
    }

    public void setRuDonated(String ruDonated) {
        this.ruDonated = ruDonated;
    }

    public String getDonateMonth() {
        return donateMonth;
    }

    public void setDonateMonth(String donateMonth) {
        this.donateMonth = donateMonth;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
