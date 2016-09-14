package com.agile.dawndev.projectclvr.Models;
import com.google.firebase.database.IgnoreExtraProperties;


 // Class used for creating candidate users
 @IgnoreExtraProperties
public class User {
    private String id;
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
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



}
