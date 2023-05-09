package com.lms.commons.web

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer
import com.fasterxml.jackson.databind.ser.std.DateSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * @author Shashwat Singh
 */
@Configuration
open class DateSerializationConfig {

    @Bean
    @Primary
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val module = JavaTimeModule()
        module.addDeserializer(LocalDate::class.java, LocalDateDeserializer(DateTimeFormatter.ISO_DATE))
        module.addSerializer(LocalDateSerializer(DateTimeFormatter.ISO_DATE))
        module.addDeserializer(Date::class.java, DateDeserializer())
        module.addSerializer(DateSerializer())
        return mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(module)
    }

}

