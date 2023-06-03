package com.lms.circulation_mgmt.service

import com.lms.circulation_mgmt.enums.BookStatus
import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.Book
import com.lms.commons.models.BookCheckout
import com.lms.commons.models.Genre
import com.lms.commons.models.IdName
import com.lms.commons.models.User
import com.lms.commons.utils.toJson
import com.lms.core.service.BookService
import com.lms.core.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import java.util.*

/**
 * @author Shashwat Singh
 */
@Service
open class BookManagementControllerService {

    private val log = LoggerFactory.getLogger(BookManagementControllerService::class.java)

    @Autowired
    private lateinit var bookManagementService: BookManagementService

    @Autowired
    private lateinit var bookService: BookService

    @Autowired
    private lateinit var userService: UserService

    open fun validateGenre(genreId : Int, loggedInUser: User): Genre {
        val genre = bookService.getGenres(genreId)
        if((genre == null) || (genre.firstOrNull() == null)) {
            log.info("validateGenre - Librarian <${loggedInUser.id}> given invalid genre id <$genreId>")
            throw ApplicationException(ApplicationExceptionTypes.INVALID_GENRE)
        }
        return genre.first()
    }

    open fun addNewBook(book: Book, loggedInUser: User): Book {
        book.genre = validateGenre(book.genre!!.id!!, loggedInUser)

       return try {
           bookManagementService.addBook(book)
           log.info("addNewBook - Librarian <${loggedInUser.id}> successfully added new book <${book.id}>")
           book
        } catch (e: DuplicateKeyException) {
           log.info("addNewBook - Librarian <${loggedInUser.id}> failed to add new book <${book.title}>")
           throw ApplicationException(ApplicationExceptionTypes.BOOK_ALREADY_EXIST)
        }
    }

    open fun checkIfBookExist(bookId: String, loggedInUser: User): Book {
        val book = bookService.getBook(bookId)
        if(book == null) {
            log.info("checkIfBookExist - Librarian <${loggedInUser.id}> given invalid book <${bookId}> detail")
            throw ApplicationException(ApplicationExceptionTypes.INVALID_BOOK_DETAIL)
        }
        return book
    }

    open fun updateBook(bookId: UUID, book: Book, loggedInUser: User): Book? {
        checkIfBookExist(bookId.toString(), loggedInUser)
        if(book.genre?.id != null) {
            book.genre = validateGenre(book.genre!!.id!!, loggedInUser)
        }
        book.id = bookId.toString()
        return try {
            bookManagementService.updateBook(book)
            log.info("updateBook - Librarian <${loggedInUser.id}> successfully updated the given book <${bookId}> details.")
            book
        } catch (e: DuplicateKeyException) {
            log.info("updateBook - Librarian <${loggedInUser.id}> given book details are already exits.")
            throw ApplicationException(ApplicationExceptionTypes.BOOK_ALREADY_EXIST)
        }
    }

    private fun checkIfMemberExist(member: User, loggedInUser: User): User {
        val memberDetail = userService.get(member)
        if(memberDetail == null) {
            log.info("issueBook - Librarian <${loggedInUser.id}> given invalid member <${member.id}> detail.")
            throw ApplicationException(ApplicationExceptionTypes.INVALID_MEMBER_DETAIL)
        }
        return member
    }

    open fun issueBook(bookCheckout: BookCheckout, loggedInUser: User): BookCheckout {
        val book = checkIfBookExist(bookCheckout.book!!.id!!, loggedInUser)
        if(book.status!!.id == BookStatus.BORROWED.id) {
            log.info("issueBook - Librarian <${loggedInUser.id}> trying to issue an book <${book.id}> which is already borrowed <${book.status!!.id}>")
            throw ApplicationException(ApplicationExceptionTypes.BOOK_ALREADY_BORROWED)
        }
        bookCheckout.book = book
        bookCheckout.member = checkIfMemberExist(bookCheckout.member!!, loggedInUser)
        bookCheckout.librarian = loggedInUser
        val insertedRow = bookManagementService.issueBook(bookCheckout)
        if(insertedRow != 1) {
            log.info("issueBook - Librarian <${loggedInUser.id}> failed to issue an book <${bookCheckout.book!!.id}> for member <${bookCheckout.member!!.id}>")
            throw ApplicationException(ApplicationExceptionTypes.GENERIC_EXCEPTION)
        }
        //Update Status for Book
       bookManagementService.updateBook(Book(book.id!!, IdName(BookStatus.BORROWED.id)))
        log.info("issueBook - Librarian <${loggedInUser.id}> successfully issue an book <${bookCheckout.book!!.id}> for member <${bookCheckout.member!!.id}>")
        return bookCheckout
    }

    open fun returnBook(bookId: String, memberId: String, loggedInUser: User) {
        val book = checkIfBookExist(bookId, loggedInUser)
        if(book.status!!.id == BookStatus.AVAILABLE.id) {
            log.info("returnBook - Librarian <${loggedInUser.id}> trying to return book <${book.id}> which is in Available State.")
            throw ApplicationException(ApplicationExceptionTypes.BOOK_IN_AVAILABLE_STATE)
        }
        val bookCheckout = BookCheckout()
        bookCheckout.book = book
        bookCheckout.member = checkIfMemberExist(User(memberId), loggedInUser)
        bookCheckout.librarian = loggedInUser
        val updatedRow = bookManagementService.returnBook(bookCheckout)
        if(updatedRow != 1) {
            log.info("returnBook - Librarian <${loggedInUser.id}> failed to return an book <${bookCheckout.book!!.id}> for member <${memberId}>")
            throw ApplicationException(ApplicationExceptionTypes.GENERIC_EXCEPTION)
        }
        //Update Status for Book to Available
        bookManagementService.updateBook(Book(bookId, IdName(BookStatus.AVAILABLE.id)))
        log.info("returnBook - Librarian <${loggedInUser.id}> successfully return book <${bookId}> for member <${memberId}>")
    }

}