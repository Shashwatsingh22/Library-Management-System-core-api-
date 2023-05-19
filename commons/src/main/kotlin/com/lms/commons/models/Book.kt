package com.lms.commons.models


/**
 * @author Shashwat Singh
 * To map with app.books
 */
class Book() {

    var id: String? = null
    var title: String? = null
    var description: String? = null
    var author: String? = null
    var shelfLocation: String? = null
    var genre: Genre? = null
    var status: IdName? = null
    var imageUrl: String? = null

    constructor(id: String, status: IdName) : this() {
        this.id = id
        this.status = status
    }
}