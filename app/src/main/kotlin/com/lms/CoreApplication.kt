package com.lms

import com.lms.auth.integrations.ip_info.models.IpInfoEnv
import com.lms.commons.models.BuildInfo
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

/**
 * @author Shashwat Singh
 * Entrypoint for Springboot Application
 */
@OpenAPIDefinition(
    servers = [Server(url = "/v1")]
)
@SpringBootApplication
@EnableConfigurationProperties(BuildInfo::class, IpInfoEnv::class)
open class CoreApplication

   fun main(args: Array<String>) {
        runApplication<CoreApplication>(*args)
    }
