package com.lms.auth.services

import com.lms.auth.dao.AuthDao
import com.lms.auth.integrations.ip_info.models.IpInfoResponse
import com.lms.auth.integrations.ip_info.service.IpInfoService
import com.lms.commons.integration.service.PasswordService
import com.lms.auth.models.FilterActiveSessions
import com.lms.auth.models.LoginFailedAttempts
import com.lms.commons.constants.Constant
import com.lms.commons.models.Country
import com.lms.commons.models.Pagination
import com.lms.commons.models.User
import com.lms.commons.models.UserSession
import com.lms.commons.utils.toJson
import com.lms.core.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.Date

/**
 * @author Shashwat Singh
 */

@Service
open class AuthService {

    private val log = LoggerFactory.getLogger(AuthService::class.java)

    @Autowired
    private lateinit var authDao: AuthDao

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var passwordService: PasswordService

    @Autowired
    private lateinit var ipInfoService: IpInfoService

    open fun insertSession(session: UserSession): Int {
        return authDao.insert(session)
    }

    open fun getUserAuthDetails(sessionId: String): UserSession? {
        return authDao.get(sessionId)
    }

    open fun logFailedAttempt(loginFailedAttempts: LoginFailedAttempts): Int {
        return authDao.logFailedAttempt(loginFailedAttempts)
    }

    open fun logout(session: UserSession, sessionId: String?): Int {
        session.id = sessionId
        session.expiryDate = Date()
        println(session.toJson())
        return authDao.update(session)
    }

    open fun getLoginFailedAttempts(loginFailedAttempts: LoginFailedAttempts): List<LoginFailedAttempts>? {
        return authDao.getLoginFailedAttempts(loginFailedAttempts)
    }

    open fun checkIfLoginAllowed(loginParam: User): Boolean {
        val failedAttempts = getLoginFailedAttempts(LoginFailedAttempts(loginParam.userName!!))
        if ((failedAttempts?.size ?: 0) >= Constant.Auth.FAILED_LOGIN_ATTEMPTS) {
            log.info("checkIfLoginAllowed - User has reached <${failedAttempts!!.size}> attempts. Aborting request for email <${loginParam.email}>")
            return false
        }
        return true
    }

    open fun validateUser(loginParam: User): User? {
        val user = userService.get(loginParam, true)
        if (user != null && passwordService.checkPassword(loginParam.password!!, user.password!!)) {
            log.info("validateUser - User <${user.id}> has been successfully validated.")
            user.password = null
            return user
        }
        log.info("validateUser - User <${loginParam.userName}> process failed to validate password.")
        return null
    }

    open fun getActiveSessions(filter: FilterActiveSessions): Pagination<UserSession>? {
        syncIpDetails(filter)
        return authDao.getActiveSessions(filter)
    }

    /**
     * @method syncIpDetails
     * This method request to ip-info to get the information about the requested IP
     * (whose details not present in our database), then it will update the app.auth_token table.
     */
    private fun syncIpDetails(filterActiveSessions: FilterActiveSessions) {
        val paginatedData = authDao.getActiveSessions(filterActiveSessions)
        if (paginatedData != null && !paginatedData.data.isNullOrEmpty()) {
            val activeSessions = paginatedData.data!!

            val ipAddresses =
                activeSessions.filter { it.country == null && it.city == null }.mapNotNull { it.ipAddress }.toSet()
            if (ipAddresses.isNotEmpty()) {
                val response = ipInfoService.getIpInformation(ipAddresses)
                if (response.statusCode == HttpStatus.OK.value()) {
                    processActiveSessionIps(response, activeSessions, filterActiveSessions)
                } else {
                    log.error("syncIpDetails - IpInfo response given statusCode ${response.statusCode} with errorMessage ${response.errorMessage}")
                }
            }
        }
    }

    private fun processActiveSessionIps(
        response: IpInfoResponse, activeSessions: List<UserSession>,
        filterActiveSessions: FilterActiveSessions
    ) {
        val ipDetails = response.ipDetails
        activeSessions.forEach {
            if (it.city.isNullOrEmpty() && it.country?.isoCode.isNullOrEmpty() && !ipDetails?.get(it.ipAddress)?.city.isNullOrEmpty()
                && !ipDetails?.get(it.ipAddress)?.country.isNullOrEmpty()
            ) {
                it.city = ipDetails?.get(it.ipAddress)?.city
                val country = Country()
                country.isoCode = ipDetails?.get(it.ipAddress)?.country
                it.country = country
                it.user = filterActiveSessions.user
                authDao.update(it)
            }
        }
        log.info("processActiveSessionIps - total rows <${activeSessions.size}> processed")
    }
}