package com.lms.circulation_mgmt.controller

import com.lms.circulation_mgmt.models.MemberFilters
import com.lms.circulation_mgmt.service.MemberControllerService
import com.lms.circulation_mgmt.service.MemberService
import com.lms.commons.annotations.AuthorizedLibrarian
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.Pagination
import com.lms.commons.models.User
import io.swagger.annotations.ApiParam
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
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author Shashwat Singh
 */

@RestController
@RequestMapping("/member")
@AuthorizedLibrarian
class MemberController {

    @Autowired
    private lateinit var memberControllerService: MemberControllerService

    @Autowired
    private lateinit var memberService: MemberService

    @Operation(summary = "Add an new member")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Basic information required, for add new members",
        required = true,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(ref = "SignupRequest")
        )]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "422",
                description = "ErrorCode: 13, Please try to do signup with different userName or Email.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = "Successfully onboarded an new member",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "UserProfileRead")
                )]
            )]
    )
    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun addMember(@RequestBody member: User, loggedInUser: User): User {
        memberControllerService.onboardMember(member, loggedInUser)
        return member
    }

    @Operation(summary = "Remove an member")
    @Parameter(
        name = "memberId",
        `in` = ParameterIn.PATH,
        description = "userId of member required to remove user.",
        required = true
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "404",
                description = "ErrorCode: 16, Member not found, please give correct member details.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "ErrorCode: 7, Something went wrong. Please try again later.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200",
                description = "Successful member removed.",
            )]
    )
    @DeleteMapping(value = ["/{memberId}"])
    fun deleteMember(@PathVariable memberId: UUID, @ApiParam(hidden = true) loggedInUser: User): ResponseEntity<Void> {
        memberControllerService.deleteMember(memberId, loggedInUser)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Update an member details")
    @Parameter(
        name = "memberId",
        `in` = ParameterIn.PATH,
        description = "Id of member required to update member details.",
        required = true
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Member object with updated details to update",
        required = true,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(ref = "UpdateMember")
        )]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "404",
                description = "ErrorCode: 16, Member not found, please give correct member details.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "ErrorCode: 7, Something went wrong. Please try again later.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200",
                description = "Successful member details successfully updated.",
                content = [Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(ref = "UserProfileRead")
                    )]
            )]
    )
    @PutMapping(value = ["/{memberId}"])
    fun updateMember(@PathVariable memberId: UUID, @RequestBody member: User, @ApiParam(hidden = true) loggedInUser: User): User {
        memberControllerService.checkIfMemberExists(memberId, loggedInUser)
        return memberControllerService.updateMemberDetails(memberId, member, loggedInUser)
    }

    @Operation(summary = "Gets paginated list of all members")
    @Parameters(
        value = [
            Parameter(
                name = "searchText",
                `in` = ParameterIn.QUERY,
                description = "For searching name or email. Match found on either username, name or email"
            ),
            Parameter(
                name = "statusIds",
                `in` = ParameterIn.QUERY,
                description = "status ids ",
                example = "6 (DELETED), 7 (ACTIVE)"
            ),
            Parameter(
                name = "collegeName",
                `in` = ParameterIn.QUERY,
                description = "For Searching by college or school name",
                example = "IIT Bombay"
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
                responseCode = "200", description = "Successfully sent the list of members",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "MembersList")
                )]
            )
        ]
    )
    @GetMapping(value = ["/list"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getClients(memberFilter: MemberFilters): Pagination<User>? {
        return memberService.getMembers(memberFilter)
    }

    @Operation(summary = "Get an member details based on its memberId.")
    @Parameter(
        name = "memberId",
        `in` = ParameterIn.PATH,
        description = "id of the member",
        required = true
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "404",
                description = "ErrorCode: 16, Member not found, please give correct member details.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = "Client Details",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "UserProfileRead")
                )]
            )
        ]
    )
    @GetMapping(value = ["/{memberId}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getClient(@PathVariable("memberId") memberId: UUID, loggedInUser: User): User {
        return memberControllerService.checkIfMemberExists(memberId, loggedInUser)
    }
}