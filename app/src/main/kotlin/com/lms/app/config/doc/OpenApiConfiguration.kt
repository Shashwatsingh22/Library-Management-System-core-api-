package com.lms.app.config.doc

import com.google.gson.Gson
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.JsonSchema
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.nio.charset.StandardCharsets

/**
 * @author Shashwat Singh
 */

@Configuration
open class OpenApiConfiguration {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    open fun openApiCustomiser(): OpenApiCustomiser {

        return OpenApiCustomiser { openApi ->

            val gson = Gson()
            val resolver = PathMatchingResourcePatternResolver(this.javaClass.classLoader)
            val resources: Array<Resource> = resolver.getResources("classpath*:json_schema/**/*.json")
            val schemas = openApi.components.schemas
            val info = Info()
            info.title = "Core API"
            info.version = "1.0"
            openApi.info(info)
            resources.forEach {
                try {
                    val jsonInString = IOUtils.toString(it.inputStream, StandardCharsets.UTF_8.name())
                    val schema = gson.fromJson(jsonInString, JsonSchema::class.java)
                    schema.name = it.filename!!.substring(0, it.filename!!.length - ".json".length)
                    schemas[schema.name]?.let {
                        throw IllegalStateException("Multiple files found for schema: ${schema.name}")
                    }
                    schemas[schema.name] = schema
                } catch (e: Exception) {
                    log.error("openApiCustomiser - failed to set schema for file: ${it.filename}")
                    throw java.lang.IllegalStateException(e)
                }
            }
        }
    }
}