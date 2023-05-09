package com.lms.app.config.web

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream

/**
 * @author Shashwat Singh
 */

open class CachedBodyServletInputStream(cachedBody: ByteArray) : ServletInputStream() {
	private var cachedBodyInputStream : InputStream?=null

	init {
		this.cachedBodyInputStream	=	ByteArrayInputStream(cachedBody)
	}

	override fun read(): Int {
		return cachedBodyInputStream!!.read();
	}

	override fun isFinished(): Boolean {
		try {
			return cachedBodyInputStream!!.available() == 0
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return false
	}

	override fun isReady(): Boolean {
		return true
	}

	override fun setReadListener(listener: ReadListener?) {
		throw UnsupportedOperationException()
	}


}
