package com.agile.dawndev.projectclvr.Models;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class UsersCompany {
    private String key;


    public UsersCompany() {}

    public UsersCompany(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
