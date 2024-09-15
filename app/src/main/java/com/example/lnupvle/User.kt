package com.example.lnupvle

class User (
    var uid: String,
    var firstname: String,
    var lastname: String,
    var email: String,
    var phone: String,
) {
    constructor() : this("", "", "", "", "")

    // Конструктор з усіма властивостями класу
    init {
        // Ініціалізуємо властивості за допомогою переданих параметрів
        this.uid = uid
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.phone = phone
    }
}