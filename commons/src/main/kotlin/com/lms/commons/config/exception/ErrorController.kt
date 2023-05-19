package com.lms.commons.config.exception

import com.fasterxml.jackson.core.JsonParseException
import com.lms.commons.constants.ApplicationExceptionTypes
import com.lms.commons.models.ApplicationException
import com.lms.commons.utils.toJson
import org.apache.commons.lang3.exception.ExceptionUtils
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * @author Shashwat Singh
 * Exception handler
 */
@ControllerAdvice
class ErrorController {

    private val log = LoggerFactory.getLogger(ErrorController::class.java)

    @ExceptionHandler(ApplicationException::class)
    fun handleCustomErrorExceptions(e: Exception): ResponseEntity<ApplicationException> {
        // casting the generic Exception e to ApplicationException
        val applicationException = e as ApplicationException
        val status = applicationException.status
        return ResponseEntity(applicationException, e.headers, status)
    }

    /**
     * @method handleTypeMismatchException
     * Handled, the exceptions occur if invalid data-type given for either Path or Query Params.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(e: MethodArgumentTypeMismatchException): ResponseEntity<ApplicationException> {
        val fieldName = e.name
        val requiredType = e.requiredType?.simpleName
        log.error("handleTypeMismatchException - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(
                ApplicationExceptionTypes.INVALID_PARAMS.first,
                ApplicationExceptionTypes.INVALID_PARAMS.second,
                ApplicationExceptionTypes.INVALID_PARAMS.third.format(fieldName, requiredType)
            ),
            ApplicationExceptionTypes.INVALID_PARAMS.second
        )
    }

    /**
     * @method handleRequestHeaderException
     * Handled, the exceptions occur if some required headers are missing.
     */
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleRequestHeaderException(e: MissingRequestHeaderException): ResponseEntity<ApplicationException> {
        log.error("handleRequestHeaderException - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(
                ApplicationExceptionTypes.MISSING_HEADER.first,
                ApplicationExceptionTypes.MISSING_HEADER.second,
                ApplicationExceptionTypes.MISSING_HEADER.third.format(e.headerName)
            ),
            ApplicationExceptionTypes.MISSING_HEADER.second
        )
    }

    /**
     * @method handleHttpMessageNotReadableException
     * Handled, the exceptions occur if invalid data-type given in request-body.
     */
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ApplicationException> {
        log.error("handleHttpMessageNotReadableException - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(
                ApplicationExceptionTypes.GENERIC_EXCEPTION
            ),
            ApplicationExceptionTypes.GENERIC_EXCEPTION.second
        )
    }

    /**
     * @method handleBindException
     * Handled, the exceptions occur when TypeException at the time of Binding.
     */
    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ResponseEntity<ApplicationException> {
        log.error("handleBindException - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(
                ApplicationExceptionTypes.REQUEST_ERROR
            ),
            ApplicationExceptionTypes.REQUEST_ERROR.second
        )
    }

    /**
     * @method handleJsonParseException
     * Handled, the exceptions occur when something wrong in json.
     */
    @ExceptionHandler(JsonParseException::class)
    fun handleJsonParseException(e: JsonParseException): ResponseEntity<ApplicationException> {
        log.error("handleJsonParseException - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(
                ApplicationExceptionTypes.REQUEST_ERROR
            ),
            ApplicationExceptionTypes.REQUEST_ERROR.second
        )
    }

    /**
     * @method handleMethodNotSupportedException
     * Handled, the exceptions occur when request supports POST but request comes with DELETE
     * then this exception occurred.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<ApplicationException> {
        log.error("handleMethodNotSupportedException - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(
                ApplicationExceptionTypes.INVALID_HTTP_METHOD_REQUEST.first,
                ApplicationExceptionTypes.INVALID_HTTP_METHOD_REQUEST.second,
                ApplicationExceptionTypes.INVALID_HTTP_METHOD_REQUEST.third.format(e.supportedMethods?.toJson())
            ),
            ApplicationExceptionTypes.INVALID_HTTP_METHOD_REQUEST.second
        )
    }


    /**
     * @method handleRequestParameterException
     * Handled, the exceptions occur when an requestParam required, but we missed it in request
     */
    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleRequestParameterException(e: MissingServletRequestParameterException): ResponseEntity<ApplicationException> {
        log.error("handleRequestParameterException - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(
                ApplicationExceptionTypes.REQUEST_ERROR
            ),
            ApplicationExceptionTypes.REQUEST_ERROR.second
        )
    }

    /**
     * Generic exception handling
     */
    @ExceptionHandler(Exception::class)
    fun handleExceptions(e: Exception): ResponseEntity<ApplicationException> {
        log.error("handleExceptions - Exception thrown - ${e.message}, stackTrace - ${ExceptionUtils.getStackTrace(e)}")
        return ResponseEntity<ApplicationException>(
            ApplicationException(ApplicationExceptionTypes.GENERIC_EXCEPTION, details = e.printStackTrace()),
            ApplicationExceptionTypes.GENERIC_EXCEPTION.second
        )
    }

}
