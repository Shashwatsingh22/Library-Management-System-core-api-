package com.lms.core.dao

import com.lms.commons.models.BookCheckout
import com.lms.commons.models.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * @author Shashwat Singh
 */

@Mapper
interface UserDao {

    fun get(@Param("user") user: User, @Param("includePassword") includePassword: Boolean): User?

    fun insert(user: User): Int

    fun update(user: User): Int

    fun getBookCheckouts(user: User): List<BookCheckout>?
}