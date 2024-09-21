package com.example.lnupvle.dataClass

data class Lesson (
    var lessonName: String,
    var lessonInfo: String,
    var lessonId: String,
    var lessonGroup: String,
    var lessonPassword: String,
    var lessonTeacher: String
) {
    constructor() : this("", "", "", "", "", "")

    init {
        this.lessonName = lessonName
        this.lessonInfo = lessonInfo
        this.lessonId = lessonId
        this.lessonGroup = lessonGroup
        this.lessonPassword = lessonPassword
        this.lessonTeacher = lessonTeacher
    }
}