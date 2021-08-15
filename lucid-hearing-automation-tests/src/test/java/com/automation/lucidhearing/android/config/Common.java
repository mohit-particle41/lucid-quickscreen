package com.automation.lucidhearing.android.config;

public enum Common {
    MOBILE_IMPLICIT_WAIT("60"),
    INVALID_FIRST_NAME("Please enter valid first name(min length: 2 characters)"),
    INVALID_LAST_NAME("Please enter valid last name(min length: 2 characters)"),
    INVALID_EMAIL_ID("Please enter valid email address"),
    INVALID_ZIP_CODE("Please enter valid zip code"),
    INVALID_PHONE_NO("Please enter valid phone number");

    String commonValue;

    private Common(String value) {
        this.commonValue = value;
    }

    public String toString() {
        return this.commonValue;
    }
}


