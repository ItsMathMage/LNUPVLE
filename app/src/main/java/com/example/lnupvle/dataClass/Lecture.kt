package com.example.lnupvle.dataClass

data class Lecture (
    var lectureName: String,
    var lectureId: String,
    var lectureFormat: String
) {
    constructor() : this("", "", "")

    init {
        this.lectureName = lectureName
        this.lectureId = lectureId
        this.lectureFormat = lectureFormat
    }
}