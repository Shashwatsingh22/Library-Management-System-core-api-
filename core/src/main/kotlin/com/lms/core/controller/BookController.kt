package com.lms.core.controller

import com.lms.commons.models.Book
import com.lms.commons.models.Genre
import com.lms.commons.models.Pagination
import com.lms.core.models.FilterBooks
import com.lms.core.service.BookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*


/**
 * @author Shashwat Singh
 */

@RestController
@RequestMapping("/book")
class BookController {

    @Autowired
    private lateinit var bookService: BookService

    @Operation(summary = "Get Genre details.")
    @Parameter(
        name = "genreId",
        `in` = ParameterIn.QUERY,
        description = "genreId required to get the details.",
        required = true
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "List of Genre Details",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "GenreLists")
                )]
            )
        ]
    )
    @GetMapping(value = ["/genre"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGenre(@RequestParam("genreId") genreId: Int?): List<Genre>? {
        return bookService.getGenres(genreId)
    }

    @Operation(summary = "Gets paginated list of filtered books")
    @Parameters(
        value = [
            Parameter(
                name = "searchText",
                `in` = ParameterIn.QUERY,
                description = "For searching Book. Match found on either title, author or genre title"
            ),
            Parameter(
                name = "statusIds",
                `in` = ParameterIn.QUERY,
                description = "status ids for book availabilities",
                example = "3 (AVAILABLE), 4 (BORROWED)"
            ),
            Parameter(
                name = "genreIds",
                `in` = ParameterIn.QUERY,
                description = "Search by genres ids"
            ),
            Parameter(
                name = "offset",
                `in` = ParameterIn.QUERY,
                description = "offset",
                example = "0"
            ),
            Parameter(
                name = "limit",
                `in` = ParameterIn.QUERY,
                description = "limit",
                example = "10"
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Successfully sent the list of books",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "BooksList")
                )]
            )
        ]
    )
    @GetMapping(value = ["/list"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBooks(filter: FilterBooks): Pagination<Book>? {
        return bookService.getBooks(filter)
    }

    @Operation(summary = "Get book details based on its bookId.")
    @Parameter(
        name = "bookId",
        `in` = ParameterIn.PATH,
        description = "id of the book",
        required = true
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Successfully Sent the Book Details",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = Book::class)
                )]
            )
        ]
    )
    @GetMapping(value = ["/{bookId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getBookDetails(@PathVariable("bookId") bookId: UUID): Book? {
        return bookService.getBook(bookId.toString())
    }
}