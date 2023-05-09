package com.lms.commons.constants

/**
 * @author Shashwat Singh
 */
object Constant {

    const val SCHEMA_LOCATION = "classpath:json_schema/%s/%s.json"
    const val REQUEST_SCHEMA = "request"
    const val RESPONSE_SCHEMA = "response"

    const val servletAttributeAuthKey = "authToken"
    const val servletAttributeRequestBodyKey = "requestBody"

    const val IP_ADDRESS = "X-FORWARDED-FOR"

    const val KEY_USER_AGENT = "user-agent"

    const val accessTokenHeaderKey = "user-auth-token"

    object Auth {
        const val COST = 12 // BCrypt (Number of Iterations)
        const val FAILED_LOGIN_ATTEMPTS = 5
        const val ACCESS_TOKEN_LENGTH = 32
    }

    const val DEFAULT_OFFSET = 0
    const val DEFAULT_LIMIT = 10

    const val TOKEN_EXPIRY_TIME = 300000 //5 min
    const val SESSION_ID = "sessionId"
}