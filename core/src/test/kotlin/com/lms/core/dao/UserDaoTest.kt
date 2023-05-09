package com.lms.core.dao

import com.lms.commons.models.User
import com.lms.app.config.db.PersistenceConfig
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

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
        Assertions.assertThat(userDao.get(User("test"), false)).isNull()
    }

    @Test
    fun get_whenPassRequired() {
        Assertions.assertThat(userDao.get(User("test"), true)).isNull()
    }
}