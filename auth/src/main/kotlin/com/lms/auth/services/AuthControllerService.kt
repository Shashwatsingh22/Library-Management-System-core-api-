package com.lms.auth.services

import com.lms.auth.models.LoginFailedAttempts
import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.User
import com.lms.commons.models.UserSession
import com.lms.commons.services.CommonService
import com.lms.commons.utils.JwtTokenUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Shashwat Singh
 */

@Service
open class AuthControllerService {

    private val log = LoggerFactory.getLogger(AuthControllerService::class.java)

    @Autowired
    private lateinit var commonsService: CommonService

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    open fun getUserDeviceDetails(ipAddress: String?, userAgent: String?): UserSession {
        val userSession = UserSession()
        userSession.ipAddress = ipAddress
        userSession.userAgent = userAgent
        userSession.deviceInfo = userAgent?.let { commonsService.parseUserAgent(it) }
        return userSession
    }

    open fun validateUser(loginParam: User, userSession: UserSession): User {
        //First check User can do log in or not
        if(!authService.checkIfLoginAllowed(loginParam)) {
            log.info("validateUser - Attempts exceeded, login not allowed for userName <${loginParam.userName}>")
            throw ApplicationException(ApplicationExceptionTypes.TOO_MANY_LOGIN_ATTEMPTS)
        }

        //Validate User
        val authenticatedUser =  authService.validateUser(loginParam)
        if(authenticatedUser == null) {
            val insertedRow = authService.logFailedAttempt(LoginFailedAttempts(loginParam.userName!!, userSession))
            log.info("validateUser - Added <$insertedRow> row for failed login attempts for email <${loginParam.userName}>")
            throw ApplicationException(ApplicationExceptionTypes.INVALID_AUTH_DETAIL)
        }

        log.info("validateUser - Successfully authenticated the user <${authenticatedUser.id}>")
        return authenticatedUser
    }

    open fun getUserSession(authenticatedUser: User, userSession: UserSession) : UserSession {
        //Remove Password
        authenticatedUser.password = null

        userSession.user = authenticatedUser

        //Add UserLogin
        val row = authService.insertSession(userSession)

        //Generate JWT token
        userSession.authToken = jwtTokenUtil.generateToken(UserSession(userSession.id!!))

        log.info("getUserSession - Created <$row> session <${userSession.id}> for user <${authenticatedUser.id}> successfully.")
        return userSession
    }

    open fun logout(session: UserSession, user: User, sessionId: UUID?) {
        val logoutParams = UserSession(user)
        logoutParams.authToken = session.authToken
        val updatedRow = authService.logout(session, sessionId?.toString())
        if (updatedRow < 1) {
            log.error("logout - Failed to logout user <${user.id}> for session <${sessionId.toString()}>")
            throw ApplicationException(ApplicationExceptionTypes.GENERIC_EXCEPTION)
        }
        log.info("updateDeposit - Successfully updated row <$updatedRow> rows for logout user <${user.id}> for session <${sessionId.toString()}>")
    }

    open fun refreshToken(session: UserSession): UserSession {
        return UserSession(authToken = jwtTokenUtil.generateToken(session))
    }
}