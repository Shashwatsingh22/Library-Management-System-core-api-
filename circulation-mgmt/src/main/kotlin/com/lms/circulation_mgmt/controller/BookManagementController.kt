package com.lms.circulation_mgmt.controller

import com.lms.circulation_mgmt.service.BookManagementControllerService
import com.lms.commons.annotations.AuthorizedLibrarian
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.Book
import com.lms.commons.models.BookCheckout
import com.lms.commons.models.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author Shashwat Singh
 */

@RestController
@RequestMapping("/books")
@AuthorizedLibrarian
class BookManagementController {

    @Autowired
    private lateinit var bookManagementControllerService: BookManagementControllerService


    @Operation(summary = "Add an new book")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Information required, to add new book",
        required = true,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(ref = "AddBook")
        )]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "422",
                description = "ErrorCode: 17, Please give correct genre details.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = "Successfully added new book.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Book::class)
                )]
            )]
    )
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addBook(@RequestBody book: Book, loggedInUser: User): Book {
        return bookManagementControllerService.addNewBook(book, loggedInUser)
    }

    @Operation(summary = "Update book details")
    @Parameter(
        name = "bookId",
        `in` = ParameterIn.PATH,
        description = "id of the book",
        required = true
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Information required, to update book details",
        required = true,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(ref = "UpdateBook")
        )]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "422",
                description = "ErrorCode: 17, Please give correct genre details." +
                              "ErrorCode: 18, Please give correct book details.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = "Successfully updated book.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Book::class)
                )]
            )]
    )
    @PutMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateBook(@RequestParam("bookId") bookId: UUID, @RequestBody book: Book, loggedInUser: User): Book {
        return bookManagementControllerService.updateBook(bookId, book, loggedInUser)
    }

    @Operation(summary = "issue an book")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Information required, to issue an book",
        required = true,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(ref = "BookIssue")
        )]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "422",
                description = "ErrorCode: 18, Please give correct book details." +
                              "ErrorCode: 20, Book Already borrowed by someone" +
                        "ErrorCode: 20, Book Already borrowed by someone",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = "Successfully added new book.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = BookCheckout::class)
                )]
            )]
    )
    @PostMapping(value = ["/issue"],produces = [MediaType.APPLICATION_JSON_VALUE])
    fun issueBook(@RequestBody bookCheckout: BookCheckout, loggedInUser: User): BookCheckout {
        return bookManagementControllerService.issueBook(bookCheckout, loggedInUser)
    }

}