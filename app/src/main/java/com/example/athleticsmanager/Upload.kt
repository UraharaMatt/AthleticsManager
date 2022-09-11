package com.example.athleticsmanager

class Upload{
    var naming: String? =""
    var url: String?=""
    constructor(){}
    constructor(naming: String?,url: String?){
        this.naming=naming
        this.url=url
    }
    /*constructor(athlete: String?,address: String?) : this() {
        if (athlete != null) {
            naming = athlete
        }
        if (address != null) {
            url = address
        }
    }*/
    fun getName(): String? {
        return naming
    }
    fun getLink(): String? {
        return url
    }
    fun setName(naming :String?){
        this.naming=naming
    }
    fun setLink(url :String?){
        this.url=url
    }
}
/*
* public class User {

    private String uid;
    private String username;
    private Boolean isMentor;
    @Nullable private String urlPicture;

    public User() { }

    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.isMentor = false;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public Boolean getIsMentor() { return isMentor; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setIsMentor(Boolean mentor) { isMentor = mentor; }
}
*
* */