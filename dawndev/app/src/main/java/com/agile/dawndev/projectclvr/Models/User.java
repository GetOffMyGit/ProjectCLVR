package com.agile.dawndev.projectclvr.Models;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;


// Class used for creating candidate users
 @IgnoreExtraProperties
public class User {
     private String id;
     private String name;
     private String email;
     private String type;
     private List<Company> companies;


    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.type = null;
        this.companies = new ArrayList<Company>();
    }


     public String getId() {
         return this.id;
     }

     public String getName() {
         return this.name;

     }

     public String getEmail() {
         return this.email;
     }

     public String getType() {
         return this.type;
     }

}
