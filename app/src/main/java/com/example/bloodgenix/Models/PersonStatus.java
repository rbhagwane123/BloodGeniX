package com.example.bloodgenix.Models;

public class PersonStatus {

    String phoneNumber;
    boolean status;

    public PersonStatus(String phoneNumber, boolean status) {
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
