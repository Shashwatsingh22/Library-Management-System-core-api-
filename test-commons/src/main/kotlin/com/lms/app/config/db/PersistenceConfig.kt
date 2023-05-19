package com.lms.app.config.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.annotations.Mapper
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

/**
 * @author Shashwat Singh
 */

@Configuration
@MapperScan(
    basePackages = ["com.lms.auth.dao", "com.lms.core.dao", "com.lms.circulation_mgmt.dao"],
    annotationClass = Mapper::class
)
open class PersistenceConfig {

    @Bean
    open fun routingDataSource(): DataSource {
        return HikariDataSource(databaseConfiguration())
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    open fun databaseConfiguration(): HikariConfig {
        return HikariConfig()
    }

}
