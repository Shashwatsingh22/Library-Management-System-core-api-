package com.lms.commons.models

import java.util.*

/**
 * @author Shashwat Singh
 * To map with app.book_checkouts
 */
class BookCheckout {
    var id: Int? = null
    var book: Book? = null
    var member: User? = null
    var librarian: User? = null
    var issueDate: Date? = null
    var returnDate: Date? = null
}