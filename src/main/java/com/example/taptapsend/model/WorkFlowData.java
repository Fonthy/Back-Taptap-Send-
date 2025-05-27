package com.example.taptapsend.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class WorkFlowData {
    public String email;

    public WorkFlowData(String email){
        this.email=email;
    }

    public String getMail(){
        return email;
    }
}
