package com.lms.commons.utils

import com.fasterxml.jackson.databind.JsonNode
import com.lms.commons.constants.Constant
import com.lms.commons.constants.Constant.servletAttributeAuthKey
import com.lms.commons.constants.Constant.servletAttributeRequestBodyKey
import com.lms.commons.models.User
import com.lms.commons.models.UserSession
import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.getUserSession(): UserSession? {
    return this.getAttribute(servletAttributeAuthKey) as? UserSession
}

fun HttpServletRequest.getAuthUser(): User? {
    return getUserSession()?.user!!
}

fun HttpServletRequest.getRequestBody(): JsonNode? {
    return this.getAttribute(servletAttributeRequestBodyKey) as? JsonNode
}

/**
 *  Ip address header has the following format
 * 	X-Forwarded-For: <client>, <proxy1>, <proxy2>
 * 	Ref: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Forwarded-For
 * 	but if user sends something in the header, then aws load balancers appends it to the client address
 * 	Ref: https://docs.aws.amazon.com/elasticloadbalancing/latest/application/x-forwarded-headers.html
 * 	Therefore second last ip address is the only way possible
 */
fun HttpServletRequest.getIpAddress(): String {
    val header = this.getHeader(Constant.IP_ADDRESS)?.split(",")
    return when (header?.size ?: 0) {
        0 -> ""
        1, 2 -> header!!.first()
        else -> header!![header.size - 2]
    }.trim()
}

fun HttpServletRequest.getUserAgent(): String {
    return this.getHeader(Constant.KEY_USER_AGENT) ?: ""
}
