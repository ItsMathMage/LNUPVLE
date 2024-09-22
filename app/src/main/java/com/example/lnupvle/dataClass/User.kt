package com.example.lnupvle.dataClass

class User (
    var uid: String,
    var firstname: String,
    var lastname: String,
    var email: String,
    var phone: String,
    var image: String
) {
    constructor() : this("", "", "", "", "", "")

    init {
        this.uid = uid
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.phone = phone
        this.image = image
    }
}