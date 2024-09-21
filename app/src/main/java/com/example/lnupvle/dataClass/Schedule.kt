package com.example.lnupvle.dataClass

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