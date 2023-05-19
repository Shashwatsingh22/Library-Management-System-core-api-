package com.lms.core.dao

import com.lms.app.config.db.PersistenceConfig
import com.lms.commons.models.User
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
class UserDaoTest {

    @Autowired
    private lateinit var userDao: UserDao

    @Test
    fun get_whenPassNotRequired() {
        Assertions.assertThat(userDao.get(User(UUID.randomUUID().toString()), false)).isNull()
    }

    @Test
    fun get_whenPassRequired() {
        Assertions.assertThat(userDao.get(User(UUID.randomUUID().toString()), true)).isNull()
    }

    @Test
    fun update() {
        Assertions.assertThat(userDao.update(User(UUID.randomUUID().toString()))).isEqualTo(0)
    }
}