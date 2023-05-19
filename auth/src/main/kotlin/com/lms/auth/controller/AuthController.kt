package com.lms.auth.controller

import com.lms.auth.models.FilterActiveSessions
import com.lms.auth.services.AuthControllerService
import com.lms.auth.services.AuthService
import com.lms.commons.annotations.SkipAuthentication
import com.lms.commons.constants.Constant
import com.lms.commons.models.ApplicationException
import com.lms.commons.models.Pagination
import com.lms.commons.models.User
import com.lms.commons.models.UserSession
import com.lms.commons.utils.getIpAddress
import com.lms.core.service.UserService
import io.swagger.annotations.ApiParam
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest

/**
 * @author Shashwat Singh
 */

@RestController
@RequestMapping("/auth")
class AuthController {

    private val log = LoggerFactory.getLogger(AuthController::class.java)

    @Autowired
    private lateinit var authControllerService: AuthControllerService

    @Autowired
    private lateinit var authService: AuthService

    @Autowired
    private lateinit var userService: UserService

    @Operation(summary = "Login a user and return user details with token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Email, Password and device information (will automatically fetched from the userAgent)",
        required = true,
        content = [Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = Schema(ref = "LoginRequest")
        )]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "429",
                description = "ErrorCode: 3, Too many failed Attempts. Please try again after 10 minutes",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "ErrorCode: 2, Invalid Email or Password, Please try with correct email or password",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = ApplicationException::class)
                )]
            ),
            ApiResponse(
                responseCode = "200", description = "Successful login",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "SessionResponse")
                )]
            )]
    )
    @SkipAuthentication
    @PostMapping(value = ["/login"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody loginParam: User, request: HttpServletRequest): UserSession {
        log.debug(
            "login - Method begins here for logging in user " + "with email <${loginParam.email}>"
        )
        val userSession = authControllerService.getUserDeviceDetails(
            request.getIpAddress(),
            request.getHeader(Constant.KEY_USER_AGENT),
        )
        val authenticatedUser = authControllerService.validateUser(loginParam, userSession)
        return authControllerService.getUserSession(authenticatedUser, userSession)
    }

    @Operation(summary = "Logout a user (all sessions - browsers or a particular session based on sessionId received)")
    @Parameter(
        name = "sessionId",
        `in` = ParameterIn.QUERY,
        description = "Id of the session which is to be logged out.",
        required = false
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successful logout",
            )]
    )
    @PostMapping(value = ["/logout"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun logout(@RequestParam sessionId: UUID?, @ApiParam(hidden = true) userSession: UserSession, @ApiParam(hidden = true) user: User): ResponseEntity<Void> {
        authControllerService.logout(userSession, user, sessionId)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Fetches list of active sessions")
    @Parameters(
        value = [
            Parameter(
                `in` = ParameterIn.QUERY,
                name = "offset",
                required = false,
                example = "0"
            ),
            Parameter(
                `in` = ParameterIn.QUERY,
                name = "limit",
                required = false,
                example = "10"
            )
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Successfully fetches the list",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "ActiveSessions")
                )]
            )]
    )
    @GetMapping(value = ["/sessions"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun listActiveSessions(@ApiParam(hidden = true) filter: FilterActiveSessions, @ApiParam(hidden = true) loggedInUser: User): Pagination<UserSession>? {
        filter.user = loggedInUser
        return authService.getActiveSessions(filter)
    }

    @Operation(summary = "Signup an user and create session user details with token")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Required User Information, device details (will automatically fetched from the userAgent)",
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
                responseCode = "200", description = "Successfully user Signed up",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "SessionResponse")
                )]
            )]
    )
    @SkipAuthentication
    @PostMapping(value = ["/signup"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signup(@RequestBody user: User, request: HttpServletRequest): UserSession {
        log.debug(
            "signup - Method begins here for signing-up an user " + "whose email is <${user.email}>"
        )
        val userSession = authControllerService.getUserDeviceDetails(
            request.getIpAddress(),
            request.getHeader(Constant.KEY_USER_AGENT),
        )
        userService.userSignup(user)
        return authControllerService.getUserSession(user, userSession)
    }

    @Operation(summary = "Refresh your token.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Successfully token refreshed.",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(ref = "RefreshTokenResponse")
                )]
            )]
    )
    @PostMapping(value = ["/refresh-token"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun refreshToken(session: UserSession): UserSession {
        return authControllerService.refreshToken(session)
    }
}