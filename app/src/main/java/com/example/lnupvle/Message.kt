package com.example.lnupvle

data class Message (
    var user: String,
    var message: String,
    var type: String,
    var time: String
    ) {
        constructor() : this("", "", "", "")

        init {
            this.user = user
            this.message = message
            this.type = type
            this.time = time
        }
}