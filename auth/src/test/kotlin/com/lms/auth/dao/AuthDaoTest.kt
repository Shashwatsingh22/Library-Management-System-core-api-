package com.lms.auth.dao

import com.lms.commons.models.UserSession
import com.lms.app.config.db.PersistenceConfig
import com.lms.auth.models.FilterActiveSessions
import com.lms.auth.models.LoginFailedAttempts
import com.lms.commons.models.DeviceInfo
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

/**
 * @author Shashwat Singh
 */

@MybatisTest
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [PersistenceConfig::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthDaoTest {

    @Autowired
    private lateinit var authDao: AuthDao

    @Test
    fun insert() {
        val session = UserSession()
        session.authToken = "abcd1234"

        val deviceInfo = DeviceInfo()
        deviceInfo.deviceName = "Iphone 5s"
        deviceInfo.osVersion = "IOS 7"
        session.deviceInfo = deviceInfo
        Assertions.assertThat(authDao.insert(UserSession())).isEqualTo(1)
    }

    @Test
    fun logFailedAttempt() {
        Assertions.assertThat(authDao.logFailedAttempt(LoginFailedAttempts())).isEqualTo(1)
    }

    @Test
    fun getLoginFailedAttempts() {
        Assertions.assertThat(authDao.getLoginFailedAttempts(LoginFailedAttempts())).isNullOrEmpty()
    }

    @Test
    fun get() {
        Assertions.assertThat(authDao.get("authToken")).isNull()
    }

    @Test
    fun update() {
        val userSession = UserSession()
        userSession.expiryDate = Date()
        Assertions.assertThat(authDao.update(userSession)).isEqualTo(0)
    }

    @Test
    fun getActiveSessions() {
        Assertions.assertThat(authDao.getActiveSessions(FilterActiveSessions())).isNull()
    }
}