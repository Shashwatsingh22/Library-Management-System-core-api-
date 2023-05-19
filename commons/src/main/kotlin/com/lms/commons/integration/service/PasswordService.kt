package com.lms.commons.integration.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.lms.commons.constants.Constant
import org.springframework.stereotype.Service

/**
 * @author Shashwat Singh
 * By the help of Bcrypt we will manage password Validation & Encryption
 */

@Service
class PasswordService {

    fun encryptPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(Constant.Auth.COST, password.toCharArray())
    }

    fun checkPassword(password: String, bcryptHash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), bcryptHash).verified
    }
}