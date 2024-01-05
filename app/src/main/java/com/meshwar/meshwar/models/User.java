package com.meshwar.meshwar.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static User instance;
    private String imageUrl;
    private String email;
    private boolean isAdmin = false;
    private String fullName;


    private long creationDate;

    private User() {
    }

    public static User getInstance(){
        if(instance == null) {
            instance = new User() ;
            return instance;
        }
        return instance;
    }

    public User(String imageUrl, String email, boolean isAdmin, String fullName, long creationDate) {
        this.imageUrl = imageUrl;
        this.email = email;
        this.isAdmin = isAdmin;
        this.fullName = fullName;
        this.creationDate = creationDate;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}
