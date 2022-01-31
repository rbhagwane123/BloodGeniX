package com.example.bloodgenix.Models;

public class BlockUser {
    String receiver;
    String sender;

    public BlockUser(){

    }

    public BlockUser(String receiver, String sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
