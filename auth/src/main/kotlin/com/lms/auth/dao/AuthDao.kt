package com.lms.auth.dao

import com.lms.auth.models.FilterActiveSessions
import com.lms.auth.models.LoginFailedAttempts
import com.lms.commons.models.Pagination
import com.lms.commons.models.UserSession
import org.apache.ibatis.annotations.Mapper

/**
 * @author Shashwat Singh
 */
@Mapper
interface AuthDao {

    fun insert(session: UserSession): Int

    fun logFailedAttempt(loginFailedAttempts: LoginFailedAttempts): Int

    fun getLoginFailedAttempts(loginFailedAttempts: LoginFailedAttempts): List<LoginFailedAttempts>?

    fun get(sessionId: String): UserSession?

    fun update(userSession: UserSession): Int

    fun getActiveSessions(filter: FilterActiveSessions): Pagination<UserSession>?
}