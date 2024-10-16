package com.example.visitorapp.Model;

public class UserModel {

//    collecting Users information
    public String userId, name,profile, city;

//
    public long coins;

//    empty constructor required
    public UserModel(){

    }

//    constructor with user info parameter
    public UserModel(String userId, String name, String profile, String city,long coins) {
        this.userId = userId;
        this.name = name;
        this.profile = profile;
        this.city = city;
        this.coins = coins;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

}
