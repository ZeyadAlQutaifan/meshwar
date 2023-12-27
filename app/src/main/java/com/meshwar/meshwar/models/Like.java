package com.meshwar.meshwar.models;

public class Like {
    private String ownerID;
    private String body;
    private String placeId;
    private long creationTime;

    public Like(String ownerID, String body, String placeId, long creationTime) {
        this.ownerID = ownerID;
        this.body = body;
        this.placeId = placeId;
        this.creationTime = creationTime;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
