package com.example.lnupvle

class Details (
    var detailsName: String,
    var detailsTeacher: String,
    var detailsTime: String,
    var detailsPlace: String,
    var detailsLink: String
    ) {
        constructor() : this("", "", "", "", "")

        init {
            this.detailsName = detailsName
            this.detailsTeacher = detailsTeacher
            this.detailsTime = detailsTime
            this.detailsPlace = detailsPlace
            this.detailsLink = detailsLink
        }
}