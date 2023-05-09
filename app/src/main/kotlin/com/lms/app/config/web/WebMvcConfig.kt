package com.lms.app.config.web

import com.lms.commons.constants.Constant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.format.datetime.standard.TemporalAccessorParser
import org.springframework.format.datetime.standard.TemporalAccessorPrinter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @author Shashwat Singh
 */

@Configuration
open class WebMvcConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var interceptorConfig: InterceptorConfig

    @Value("\${domain-web}")
    private var domainWeb: String = ""

    private var servletRequestArgumentResolver = ServletRequestArgumentResolver()

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(interceptorConfig)
            .addPathPatterns("/**")
    }

    override fun addFormatters(registry: FormatterRegistry) {
        super.addFormatters(registry)
        registry.addFormatterForFieldType(
            LocalDate::class.java,
            TemporalAccessorPrinter(DateTimeFormatter.ISO_DATE),
            TemporalAccessorParser(LocalDate::class.java, DateTimeFormatter.ISO_DATE)
        )
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        addCorsHeaders(
            registry.addMapping("/**"),
            domainWeb
        )
    }

    private fun addCorsHeaders(registry: CorsRegistration, domain: String) {
        registry.allowedOrigins(domain)
            .allowedMethods(
                HttpMethod.GET.name,
                HttpMethod.POST.name,
                HttpMethod.PUT.name,
                HttpMethod.DELETE.name,
                HttpMethod.OPTIONS.name
            )
            .allowedHeaders(
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.CONTENT_LENGTH,
                HttpHeaders.ACCEPT,
                HttpHeaders.ORIGIN,
                HttpHeaders.REFERER,
                HttpHeaders.USER_AGENT,
                Constant.accessTokenHeaderKey
            )
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver?>) {
        argumentResolvers.add(servletRequestArgumentResolver)
    }
}