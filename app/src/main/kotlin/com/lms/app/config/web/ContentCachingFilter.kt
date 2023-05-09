package com.lms.app.config.web

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Shashwat Singh
 */

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "ContentCachingFilter", urlPatterns = ["/*"])
open class ContentCachingFilter() : OncePerRequestFilter() {

	@Override
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val cachedBodyHttpServletRequest = CachedBodyHttpServletRequest(request)
		filterChain.doFilter(cachedBodyHttpServletRequest,response)
	}
}
