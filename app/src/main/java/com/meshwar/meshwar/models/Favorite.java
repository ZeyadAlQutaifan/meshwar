package com.meshwar.meshwar.models;

public class Favorite {
    private String userId ;
    private String placeId ;
    private long creationDate ;

    public Favorite() {
    }

    public Favorite(String userId, String placeId, long creationDate) {
        this.userId = userId;
        this.placeId = placeId;
        this.creationDate = creationDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }
}
