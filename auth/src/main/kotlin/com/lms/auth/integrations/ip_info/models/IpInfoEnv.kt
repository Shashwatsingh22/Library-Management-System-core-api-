package com.lms.auth.integrations.ip_info.models

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author Shashwat Singh
 * Variables which are required for using ip-info services
 */

@ConstructorBinding
@ConfigurationProperties(prefix = "ip-info")
data class IpInfoEnv(val token : String)
