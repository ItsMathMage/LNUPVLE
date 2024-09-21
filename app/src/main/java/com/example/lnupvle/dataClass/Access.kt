package com.example.lnupvle.dataClass

data class Access (
    var userId: String,
    var lessonId: String,
    var lessonName: String,
    var teacherName: String
) {
    constructor() : this("", "", "", "")

    init {
        this.userId = userId
        this.lessonId = lessonId
        this.lessonName = lessonName
        this.teacherName = teacherName
    }
}