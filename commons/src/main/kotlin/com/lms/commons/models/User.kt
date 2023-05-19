package com.lms.commons.models

/**
 * @author Shashwat Singh
 * To map with table app.users
 */
class User() {
    var id: String? = null
    var name: String? = null
    var profileImgUrl: String = "https://fastly.picsum.photos/id/342/2896/1944.jpg?hmac=_2cYDHi2iG1XY53gvXOrhrEWIP5R5OJlP7ySYYCA0QA"
    var userName: String? = null
    var email: String? = null
    var password: String? = null
    var role: IdName? = null
    var status: IdName? = null
    var designation: String? = null
    var college: String? = null
    var deletedBy: User? = null

    constructor(id: String) : this() {
        this.id = id
    }
}