package com.meshwar.meshwar.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Place implements Serializable {
    private static  Place instance ;
    private String title;
    private List<String> images = new ArrayList<>();
    private int views;
    private String description;
    private double lng;
    private double lat;
    private double rate;
    private int rates;
    private String city;

    private String writerId ;

    private long createdAt ;
    private Place() {
    }

    public static Place getInstance() {
        if(instance == null){
            instance = new Place();
        }
        return instance;
    }
    public static void destroy(){
        instance = null ;
    }

    public Place(String title, List<String> images, int views, String description, double lng, double lat, double rate, int rates, String city, String writerId, long createdAt) {
        this.title = title;
        this.images = images;
        this.views = views;
        this.description = description;
        this.lng = lng;
        this.lat = lat;
        this.rate = rate;
        this.rates = rates;
        this.city = city;
        this.writerId = writerId;
        this.createdAt = createdAt;
    }

    public static void setInstance(Place instance) {
        Place.instance = instance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getRates() {
        return rates;
    }

    public void setRates(int rates) {
        this.rates = rates;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public  String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
