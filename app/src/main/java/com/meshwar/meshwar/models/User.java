package com.meshwar.meshwar.models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String imageUrl ;
    private String email ;
    private boolean isAdmin = false;
    private String fullName;
    private List<String> favPlaces = new ArrayList<>();
    private List<String> reviewedPlaces = new ArrayList<>();;
    private long creationDate ;
    public User() {
    }

    public User(String imageUrl, String email, boolean isAdmin, String fullName, List<String> favPlaces, List<String> reviewedPlaces, long creationDate) {
        this.imageUrl = imageUrl;
        this.email = email;
        this.isAdmin = isAdmin;
        this.fullName = fullName;
        this.favPlaces = favPlaces;
        this.reviewedPlaces = reviewedPlaces;
        this.creationDate = creationDate;
    }

    public User(String imageUrl, String email, boolean isAdmin, String fullName, List<String> favPlaces, List<String> reviewedPlaces) {
        this.imageUrl = imageUrl;
        this.email = email;
        this.isAdmin = isAdmin;
        this.fullName = fullName;
        this.favPlaces = favPlaces;
        this.reviewedPlaces = reviewedPlaces;
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

    public List<String> getFavPlaces() {
        return favPlaces;
    }

    public void setFavPlaces(List<String> favPlaces) {
        this.favPlaces = favPlaces;
    }

    public List<String> getReviewedPlaces() {
        return reviewedPlaces;
    }

    public void setReviewedPlaces(List<String> reviewedPlaces) {
        this.reviewedPlaces = reviewedPlaces;
    }
}
