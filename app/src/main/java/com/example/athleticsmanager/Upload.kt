package com.example.athleticsmanager

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Upload{
    var filename: String? =""
    var url: String?=""
    //var usermail: String?=""
    constructor(){}
    constructor(filename: String?,url: String? ){
        this.filename=filename
        this.url=url
        //this.usermail=usermail
    }
}
    /*constructor(athlete: String?,address: String?) : this() {
        if (athlete != null) {
            filename = athlete
        }
        if (address != null) {
            url = address
        }
    }*/
    /*@JvmName("getFilename1")
    fun getFilename(): String? { return filename }
    @JvmName("getUrl1")
    fun getUrl(): String? { return url }
    @JvmName("setFilename1")
    fun setFilename(filename :String?){ this.filename=filename }
    @JvmName("setUrl1")
    fun setUrl(url :String?){ this.url=url }
    /*fun getMail(): String? {
    return usermail*/
}
fun setMail(mail :String?){
    this.usermail=mail
}*/

/*
 public class User {

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