package com.lms.app.config.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import javax.sql.DataSource

/**
 * @author Shashwat Singh
 */

@Configuration
@MapperScan(
    basePackages = ["com.lms.auth.dao", "com.lms.core.dao"],
    annotationClass = Mapper::class
)
open class PersistenceConfig {

    @Bean
    @Throws(Exception::class)
    open fun sqlSessionFactoryBean(dataSource: DataSource): SqlSessionFactory {
        val sqlSessionFactoryBean = SqlSessionFactoryBean()
        sqlSessionFactoryBean.setDataSource(dataSource)
        sqlSessionFactoryBean.setConfigLocation(ClassPathResource("mybatis-config.xml"))
        return sqlSessionFactoryBean.getObject() as SqlSessionFactory
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource")
    open fun dbConfiguration(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    open fun dataSource(): DataSource {
        return HikariDataSource(dbConfiguration())
    }
}