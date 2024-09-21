package com.example.lnupvle.dataClass

data class ScheduleTempUser (
    var scheduleId: String,
    var scheduleGroup: String
) {
    constructor() : this("","")

    init {
        this.scheduleId = scheduleId
        this.scheduleGroup = scheduleGroup
    }
}