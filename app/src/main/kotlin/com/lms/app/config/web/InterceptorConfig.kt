package com.lms.app.config.web

import com.lms.auth.services.AuthService
import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.constants.Constant
import com.lms.commons.enums.UserRole
import com.lms.commons.models.ApplicationException
import com.lms.commons.utils.JwtTokenUtil
import com.lms.commons.utils.LogPrefixes
import com.lms.commons.utils.getAuthUser
import com.lms.commons.web.ResourceType
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Shashwat Singh
 */

@Component
class InterceptorConfig : HandlerInterceptor {

    private var log = LoggerFactory.getLogger(InterceptorConfig::class.java)

    @Autowired
    private lateinit var commonWebConfigServices: CommonWebConfigServices

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil


    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.debug("preHandle - Method begins here to process request from path <${request.requestURL}>")

        if (request.method == RequestMethod.OPTIONS.name) {
            return true
        }

        commonWebConfigServices.setRequestBody(request)

        val methodAnnotationChecker: (t: Class<out Annotation>) -> Boolean =
            { (handler as? HandlerMethod)?.hasMethodAnnotation(it) == true }
        val classAnnotationChecker: (t: Class<out Annotation>) -> Boolean =
            { (handler as? HandlerMethod)?.beanType?.isAnnotationPresent(it) == true }
        var sessionId: String? = null

        when (ResourceType.getResourceType(
            request.servletPath,
            methodAnnotationChecker,
            classAnnotationChecker
        )) {
            ResourceType.OPEN, ResourceType.ACTUATOR, ResourceType.SWAGGER -> {
                request.requestURL.toString().contains("v1/ping")
            }

            ResourceType.USER_AUTHENTICATED -> {
                sessionId = validateAccessToken(request)
            }

            ResourceType.AUTHORIZED_LIBRARIAN -> {
                sessionId = validateAccessToken(request)
                isUserAllowed(request)
            }
        }
        sessionId?.let {
            MDC.put("values", LogPrefixes.SESSION_ID.format(it))
        } ?: run {
            MDC.remove("values")
        }

        if (request.method == RequestMethod.POST.name || request.method == RequestMethod.PUT.name) {
            commonWebConfigServices.validateIncomingRequestBody(request, handler)
        }
        return true
    }

    private fun isUserAllowed(request: HttpServletRequest) {
        val user = request.getAuthUser()
        if(user!!.role!!.id != UserRole.LIBRARIAN.id) {
            log.info("isUserAllowed - LoggedInUser <${user.id}> not allowed to access these endpoint.")
           throw ApplicationException(ApplicationExceptionTypes.UNAUTHORIZED)
        }
    }

    private fun validateAccessToken(request: HttpServletRequest): String {
        request.getHeader(Constant.accessTokenHeaderKey)?.let { token ->
            jwtTokenUtil.validateTokenFetchSessionId(token).let { sessionId ->
                authService.getUserAuthDetails(sessionId)?.let { session ->
                    request.setAttribute(Constant.servletAttributeAuthKey, session)
                    return session.id!!
                } ?: kotlin.run {
                    log.info("validateAccessToken - sessionId <$sessionId> already expired.")
                }
            }
        } ?: run {
            log.debug("validateAccessToken - access token not found in headers")
        }
        throw ApplicationException(ApplicationExceptionTypes.UNAUTHORIZED)
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        commonWebConfigServices.setSecurityHeaders(response)
    }
}