package com.example.lnupvle

data class Lecture (
    var lectureName: String,
    var lectureId: String
) {
    constructor() : this("", "")

    init {
        this.lectureName = lectureName
        this.lectureId = lectureId
    }
}