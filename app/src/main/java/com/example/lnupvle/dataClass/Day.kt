package com.example.lnupvle.dataClass

data class Day (
    var dayName: String,
    var key: String
) {
    constructor() : this("", "")

    init {
        this.dayName = dayName
        this.key = key
    }
}