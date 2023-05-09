package com.lms.commons.models

import java.util.*

/**
 * @author Shashwat Singh
 * To map with app.user_sessions table
 */
class UserSession() {
    var id: String? = null
    var user: User? = null
    var authToken: String? = null
    var ipAddress: String? = null
    var deviceInfo: DeviceInfo? = null
    var userAgent: String? = null
    var city: String? = null
    var country: Country? = null
    var expiryDate: Date? = null
    var addDate: Date? = null

    constructor(user: User) : this() {
        this.user = user
    }

    constructor(id: String? = null, authToken: String? = null) : this() {
        this.id = id
        this.authToken = authToken
    }
}