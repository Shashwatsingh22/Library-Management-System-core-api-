package com.lms.commons.constants

import org.springframework.http.HttpStatus

object ApplicationExceptionTypes {

    val JSON_SCHEMA_VALIDATION_ERROR = Triple(1, HttpStatus.BAD_REQUEST, "Something is wrong with the request body.")
    val INVALID_AUTH_DETAIL = Triple(2, HttpStatus.BAD_REQUEST, "Invalid Email or Password, Please try with correct email or password.")

    val INVALID_PARAMS = Triple(4, HttpStatus.BAD_REQUEST, "Invalid value for `%s`, expected type `%s`")
    val MISSING_HEADER = Triple(5, HttpStatus.BAD_REQUEST, "Required header `%s` is missing in your request.")
    val REQUEST_ERROR = Triple(6, HttpStatus.BAD_REQUEST, "Please check your request.")
    val GENERIC_EXCEPTION = Triple(7, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again later.")
    val TOO_MANY_LOGIN_ATTEMPTS = Triple(8, HttpStatus.TOO_MANY_REQUESTS, "Too many failed requests. Please try again after 10 minutes")

    val INVALID_HTTP_METHOD_REQUEST = Triple(9, HttpStatus.METHOD_NOT_ALLOWED, "Please request with correct method. Supported Methods are `%s`.")

    val UNAUTHORIZED = Triple(10, HttpStatus.UNAUTHORIZED, "Unauthorized")

    val EMAIL_ALREADY_EXISTS = Triple(11, HttpStatus.UNPROCESSABLE_ENTITY, "Given email address is already taken by other user.")
    val USER_NAME_ALREADY_EXISTS = Triple(12, HttpStatus.UNPROCESSABLE_ENTITY, "Given username is already taken by other user.")
    val EMAIL_USERNAME_ALREADY_TAKEN = Triple(13, HttpStatus.UNPROCESSABLE_ENTITY, "Please try to do signup with different userName or Email.")

    val INVALID_JWT_TOKEN = Triple(14, HttpStatus.UNAUTHORIZED,"Invalid token received, please check once." )
    val JWT_TOKEN_EXPIRED = Triple(15, HttpStatus.UNAUTHORIZED, "Given Token Expired, please again try to do login.")

    val MEMBER_NOT_FOUND =  Triple(16, HttpStatus.NOT_FOUND, "Member not found, please give correct member details")
    val INVALID_GENRE = Triple(17, HttpStatus.UNPROCESSABLE_ENTITY, "Please give correct genre details.")
    val INVALID_BOOK_DETAIL = Triple(18, HttpStatus.UNPROCESSABLE_ENTITY, "Please give correct book details.")
    val INVALID_MEMBER_DETAIL = Triple(19, HttpStatus.UNPROCESSABLE_ENTITY, "Please give correct member detail.")
    val BOOK_ALREADY_BORROWED = Triple(20, HttpStatus.UNPROCESSABLE_ENTITY, "Book Already borrowed by someone.")

}