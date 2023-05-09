package com.lms.auth.models

import com.lms.commons.models.UserSession

/**
 * @author Shashwat Singh
 * For Mapping with app.login_failed_attempts
 */
class LoginFailedAttempts() {
    var userName: String? = null
    var ipAddress: String? = null
    var userAgent: String? = null
    var addDate: String? = null

    constructor(userName: String): this() {
        this.userName = userName
    }

    constructor(userName: String, session: UserSession): this() {
        this.userName = userName
        this.ipAddress = session.ipAddress
        this.userAgent = session.userAgent
    }
}