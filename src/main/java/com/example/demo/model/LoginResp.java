package com.example.demo.model;

public class LoginResp {
    private String token;

    public LoginResp(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
