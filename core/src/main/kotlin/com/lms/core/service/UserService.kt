package com.lms.core.service

import com.lms.commons.models.User
import com.lms.core.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Shashwat Singh
 */

@Service
open class UserService {

    @Autowired
    private lateinit var userDao: UserDao

    open fun get(user: User, includePassword: Boolean = false): User? {
        return userDao.get(user, includePassword)
    }

    open fun insert(user: User): Int {
        return userDao.insert(user)
    }

}