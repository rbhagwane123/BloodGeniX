package com.example.bloodgenix.Models;

public class RecipientDetails {
    String uid;
    String bGroupRecipient;
    String locationRecipient, otherRequirement;
    String reasonBloodRequest,phoneNumber;
    public String personFullName;
    public String personProfile;

    public String getPersonFullName() {
        return personFullName;
    }

    public void setPersonFullName(String personFullName) {
        this.personFullName = personFullName;
    }

    public String getPersonProfile() {
        return personProfile;
    }

    public void setPersonProfile(String personProfile) {
        this.personProfile = personProfile;
    }

    public RecipientDetails(){

    }

    public String getReasonBloodRequest() {
        return reasonBloodRequest;
    }

    public void setReasonBloodRequest(String reasonBloodRequest) {
        this.reasonBloodRequest = reasonBloodRequest;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RecipientDetails(String uid, String bGroupRecipient, String locationRecipient, String otherRequirement, String reasonBloodRequest, String phoneNumber, String personFullName, String personProfile) {
        this.uid = uid;
        this.bGroupRecipient = bGroupRecipient;
        this.locationRecipient = locationRecipient;
        this.otherRequirement = otherRequirement;
        this.reasonBloodRequest = reasonBloodRequest;
        this.phoneNumber = phoneNumber;
        this.personFullName = personFullName;
        this.personProfile = personProfile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getbGroupRecipient() {
        return bGroupRecipient;
    }

    public void setbGroupRecipient(String bGroupRecipient) {
        this.bGroupRecipient = bGroupRecipient;
    }

    public String getLocationRecipient() {
        return locationRecipient;
    }

    public void setLocationRecipient(String locationRecipient) {
        this.locationRecipient = locationRecipient;
    }

    public String getOtherRequirement() {
        return otherRequirement;
    }

    public void setOtherRequirement(String otherRequirement) {
        this.otherRequirement = otherRequirement;
    }
}
