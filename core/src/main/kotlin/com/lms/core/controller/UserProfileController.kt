package com.lms.core.controller

import com.lms.commons.models.User
import io.swagger.annotations.ApiParam
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Shashwat Singh
 */

@RestController
@RequestMapping("/user")
class UserProfileController {

    @Operation(summary = "Gets Profile of a user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Profile of a user",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "UserProfileRead")
                )]
            )]
    )
    @GetMapping(value = ["/profile"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getUserProfile(@ApiParam(hidden = true) loggedInUser: User): User {
        return loggedInUser
    }
}