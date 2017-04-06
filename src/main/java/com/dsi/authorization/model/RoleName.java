package com.dsi.authorization.model;

/**
 * Created by sabbir on 12/19/16.
 */
public enum RoleName {

    LEAD("Lead"), HR("HR"), MANAGER("Manager"), MEMBER("Member");
    private String value;

    RoleName(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
