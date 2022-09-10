package com.example.athleticsmanager

class Upload (){
    var naming: String? = ""
    var url: String? = ""

    constructor(athlete: String?,address: String?) : this() {
        if (athlete != null) {
            naming = athlete
        }
        if (address != null) {
            url = address
        }
    }
    fun getName(): String? {
        return naming
    }
    fun getLink(): String? {
        return url
    }

}