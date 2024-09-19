package com.example.lnupvle

data class Schedule (
    var scheduleId: String,
    var scheduleGroup: String
) {
    constructor() : this("", "")

    init {
        this.scheduleId = scheduleId
        this.scheduleGroup = scheduleGroup
    }
}