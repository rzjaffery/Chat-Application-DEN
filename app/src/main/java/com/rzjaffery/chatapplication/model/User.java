package com.rzjaffery.chatapplication.model;

public class User {
    public String uid;
    public String name;
    public String email;
    public String photoUrl;
    public String fcmToken;
    public boolean online;

    public User() {} // Firebase required

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = "";
        this.fcmToken = "";
        this.online = false;
    }
}
