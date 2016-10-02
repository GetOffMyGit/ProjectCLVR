package com.agile.dawndev.projectclvr.Models;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class UsersCompany {
    private boolean key;


    public UsersCompany() {}

    public UsersCompany(Boolean key) {
        this.key = key;
    }

    public Boolean getKey() {
        return this.key;
    }

    public void setKey(Boolean key) {
        this.key = key;
    }

}
