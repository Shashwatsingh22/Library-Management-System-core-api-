package com.lms.commons.models

/**
 * @author Shashwat Singh
 * For Type Management
 */

class IdName() {
    var id: Int? = null
    var name: String? = null

    constructor(id: Int, name: String? = null) : this() {
        this.id = id
        this.name = name
    }
}