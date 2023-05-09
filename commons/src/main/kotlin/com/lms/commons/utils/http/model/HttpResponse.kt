package com.lms.commons.utils.http.model

import org.springframework.http.HttpStatus

/**
 * @author Shashwat Singh
 */

class HttpResponse<T>(val statusCode: Int?, val response: T?, val errorBody: String? = null, val exception: Exception? = null) {

	fun isResponseOk(): Boolean {
		return this.statusCode == HttpStatus.OK.value()
	}

	fun isExpectedStatusCode(statusCode: Int): Boolean {
		return this.statusCode == statusCode
	}
}
