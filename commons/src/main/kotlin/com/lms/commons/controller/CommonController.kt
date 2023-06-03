package com.lms.commons.controller

import com.lms.commons.annotations.SkipAuthentication
import com.lms.commons.models.IdName
import com.lms.commons.services.CommonService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author Shashwat Singh
 * These Controllers can be used for common data which may require for signup or in searching etc.
 */

@RestController
@RequestMapping("/common")
@SkipAuthentication
class CommonController {

    @Autowired
    private lateinit var commonService: CommonService

    /**
     * By help of this controller we can fetch any specific types by groupId
     * for-example - UserRoleType, Status, BookStatus etc
     */
    @Operation(summary = "Get types by given groupId")
    @Parameter(
        name = "groupId",
        `in` = ParameterIn.PATH,
        description = "groupId required to get specific types.",
        example = "BookStatus (GroupId = 2), Role (GroupId = 1), Status (GroupId = 3)",
        required = true
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Successfully Sent Types",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = ArraySchema(schema = Schema(implementation = IdName::class))
                )]
            )
        ]
    )
    @GetMapping(value= ["/types/{groupId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTypes(
        @PathVariable("groupId") groupId : Int
    ) : List<IdName>? {
        return commonService.getTypes(groupId)
    }
}