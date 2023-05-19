package com.lms.core.service

import com.lms.commons.models.Book
import com.lms.commons.models.Genre
import com.lms.commons.models.Pagination
import com.lms.core.dao.BookDao
import com.lms.core.models.FilterBooks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @author Shashwat Singh
 */

@Service
open class BookService {

    @Autowired
    private lateinit var bookDao: BookDao

    open fun getGenres(genreId: Int? = null): List<Genre>? {
        return bookDao.getGenres(genreId)
    }

    open fun getBooks(filter: FilterBooks): Pagination<Book>? {
        return bookDao.getBooks(filter)
    }

    open fun getBook(bookId: String): Book? {
        val param = FilterBooks()
        param.bookId = bookId
        val bookPagination = getBooks(param)
        return bookPagination?.data?.first()
    }

}