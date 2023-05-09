package com.lms.app.controller

import com.lms.commons.annotations.SkipAuthentication
import com.lms.commons.models.BuildInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/ping")
@SkipAuthentication
open class PingController(private val buildInfo: BuildInfo) {

    @Operation(summary = "Gets a static response")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Version of app",
                content = [Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = Schema(implementation = BuildInfo::class)
                )]
            )]
    )
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    open fun getPing(): BuildInfo {
        return buildInfo
    }
}
