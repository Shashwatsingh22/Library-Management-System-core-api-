package com.lms.core.dao

import com.lms.commons.models.Pagination
import com.lms.commons.models.Book
import com.lms.core.models.FilterBooks
import com.lms.commons.models.Genre
import org.apache.ibatis.annotations.Mapper

/**
 * @author Shashwat Singh
 */

@Mapper
interface BookDao {

    fun getGenres(genreId: Int?): List<Genre>?

    fun getBooks(filter: FilterBooks): Pagination<Book>?
}