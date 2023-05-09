package com.lms.app.config.web

import org.springframework.util.StreamUtils
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStreamReader
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

/**
 * @author Shashwat Singh
 */

open class CachedBodyHttpServletRequest(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
	var cachedBody : ByteArray? = null

	init{
		val inputStream : ServletInputStream? = request.inputStream;
		this.cachedBody = StreamUtils.copyToByteArray(inputStream)
	}

	@Throws(IOException::class)
	override fun getInputStream(): ServletInputStream? {
		return CachedBodyServletInputStream(cachedBody!!)
	}

	@Throws(IOException::class)
	override fun getReader(): BufferedReader? {
		val byteArrayInputStream  = ByteArrayInputStream(this.cachedBody)
		return BufferedReader(InputStreamReader(byteArrayInputStream))
	}
}
