package com.lms.core.service

import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.enums.UserStatus
import com.lms.commons.integration.service.PasswordService
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.IdName
import com.lms.commons.models.User
import com.lms.core.dao.UserDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service

/**
 * @author Shashwat Singh
 */

@Service
open class UserService {

    private val log = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private lateinit var userDao: UserDao

    @Autowired
    private lateinit var passwordService: PasswordService

    open fun get(user: User, includePassword: Boolean = false): User? {
        return userDao.get(user, includePassword)
    }

    open fun insert(user: User): Int {
        return userDao.insert(user)
    }

    open fun update(user: User): Int {
        return userDao.update(user)
    }

    open fun userSignup(user: User) {
        try {
            user.password = passwordService.encryptPassword(user.password!!)
            user.status = IdName(UserStatus.ACTIVE.id)
            insert(user)
            log.info("userSignup - User <${user.id}> successfully signedUp.")
        } catch (e: DuplicateKeyException) {
            log.info("userSignup - User had entered email <${user.email}> or userName <${user.userName}> already taken by other user.")
            throw ApplicationException(ApplicationExceptionTypes.EMAIL_USERNAME_ALREADY_TAKEN)
        }
    }

}