package com.example.bloodgenix.Models;

public class Users
{
    String uid;
    String profileImg, FullName, UserName ;
    String EmailId, password,d_o_b, Gender;
    String IdentityProof, MobileNumber;

    public Users(){

    }
    public Users(String uid, String profileImg, String fullName, String userName, String emailId, String password, String d_o_b, String gender, String identityProof, String mobileNumber) {
        this.uid = uid;
        this.profileImg = profileImg;
        FullName = fullName;
        UserName = userName;
        EmailId = emailId;
        this.password = password;
        this.d_o_b = d_o_b;
        Gender = gender;
        IdentityProof = identityProof;
        MobileNumber = mobileNumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getD_o_b() {
        return d_o_b;
    }

    public void setD_o_b(String d_o_b) {
        this.d_o_b = d_o_b;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getIdentityProof() {
        return IdentityProof;
    }

    public void setIdentityProof(String identityProof) {
        IdentityProof = identityProof;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }
}
