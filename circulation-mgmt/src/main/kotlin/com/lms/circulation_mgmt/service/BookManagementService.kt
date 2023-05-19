package com.lms.circulation_mgmt.service

import com.lms.circulation_mgmt.dao.BookManagementDao
import com.lms.commons.models.Book
import com.lms.commons.models.BookCheckout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Shashwat Singh
 */
@Service
open class BookManagementService {

    @Autowired
    private lateinit var bookManagementDao: BookManagementDao

    open fun addBook(book: Book): Int {
        return bookManagementDao.insert(book)
    }

    open fun updateBook(book: Book): Int {
        return bookManagementDao.update(book)
    }

    open fun issueBook(bookCheckout: BookCheckout): Int {
        return bookManagementDao.issueBook(bookCheckout)
    }
}