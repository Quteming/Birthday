package com.example.qtm.birthdates.Model.Bean;

public class LoginUserBean {
    private String phoneId;

    public LoginUserBean(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }
}
