package com.lms.circulation_mgmt.dao

import com.lms.commons.models.BookCheckout
import com.lms.commons.models.Book
import org.apache.ibatis.annotations.Mapper

/**
 * @author Shashwat Singh
 */

@Mapper
interface BookManagementDao {

    fun insert(book: Book): Int

    fun update(book: Book): Int

    fun issueBook(bookCheckout: BookCheckout): Int

    fun updateBookCheckout(bookCheckout: BookCheckout): Int
}