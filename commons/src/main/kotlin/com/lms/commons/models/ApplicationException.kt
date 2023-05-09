package com.lms.commons.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus

/**
 * @author Shashwat Singh
 * Handling Exceptions at the same place.
 */

@JsonIgnoreProperties("cause", "stackTrace", "status", "suppressed", "localizedMessage", "headers")
data class ApplicationException(
    var code: Int,
    var status: HttpStatus,
    override val message: String,
    var details: Any? = null,
    var headers: HttpHeaders? = null
) : Exception() {

    constructor(
        codeMessageTriplet: Triple<Int, HttpStatus, String>,
        headers: HttpHeaders? = null,
        details: Any? = null
    ) : this(
        codeMessageTriplet.first,
        codeMessageTriplet.second,
        codeMessageTriplet.third,
        headers = headers,
        details = details
    )
}