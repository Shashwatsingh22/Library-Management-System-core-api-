package com.lms.commons.models

/**
 * @author Shashwat Singh
 * Model class to map lists with total counts
 */
class Pagination<T>() {

    var count: Int? = null
    var data: List<T>? = null

    constructor(data: List<T>): this() {
        this.count = data.size
        this.data = data
    }

}
