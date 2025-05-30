package com.example.taptapsend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class EmailDetails {
    public String email;
    public String message;
    public int count;
    public boolean subscribed;

    public EmailDetails(){}

    public EmailDetails(String email,String message, int count,boolean subscribed){
        this.email=email;
        this.message=message;
        this.count=count;
        this.subscribed=subscribed;
    }
}
