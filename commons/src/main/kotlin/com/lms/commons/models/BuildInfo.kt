package com.lms.commons.models

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Shashwat Singh
 * Object depicting version of app
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "build-info")
data class BuildInfo(val version: String, val environment: String)

