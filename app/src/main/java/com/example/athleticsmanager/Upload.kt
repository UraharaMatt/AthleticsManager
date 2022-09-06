package com.example.athleticsmanager

class Upload {
    var naming: String? = ""
    var url: String? = ""
    constructor(){
        var naming: String? = ""
        var url: String? = ""
    }
    constructor(athlete: String?,address: String?){
        naming = athlete
        url = address
    }
    fun getName(): String? {
        return naming
    }
    fun getLink(): String? {
        return url
    }

}