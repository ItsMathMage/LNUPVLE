package com.example.lnupvle.dataClass

data class ChatsAccess (
    var chatId: String,
    var userId: String,
    var chatName: String
) {
    constructor() : this("", "", "")

    init {
        this.chatId = chatId
        this.userId = userId
        this.chatName = chatName
    }
}