package com.lms.app.config.web

import com.lms.commons.models.User
import com.lms.commons.models.UserSession
import com.lms.commons.utils.getAuthUser
import com.lms.commons.utils.getUserSession
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import javax.servlet.http.HttpServletRequest

/**
 * @author Shashwat Singh
 * This is an argument resolver to resolve servlet request values
 * to use it as method argument in the API instead of HttpServletRequest object
 */

class ServletRequestArgumentResolver: HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == UserSession::class.java ||
                parameter.parameterType == User::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request: HttpServletRequest = webRequest.nativeRequest as HttpServletRequest
        return when(parameter.parameterType) {
            UserSession::class.java -> request.getUserSession()
            User::class.java -> request.getAuthUser()
            else -> null
        }
    }


}