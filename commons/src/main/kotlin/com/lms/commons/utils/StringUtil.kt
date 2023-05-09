package com.lms.commons.utils

import com.google.gson.Gson
import java.security.SecureRandom

/**
 * @author Shashwat Singh
 */
object StringUtil {

    /**
     * Gets secure random string
     */
    fun getSecureRandomString(length: Int) : String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        val random = SecureRandom()
        return random.ints(0, chars.length)
            .limit(length.toLong())
            .collect(::StringBuilder, { t, value -> t.append(chars[value]) }, StringBuilder::append).toString()
    }
}

fun Any.toJson(): String {
    return Gson().toJson(this)
}