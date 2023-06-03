package com.lms.circulation_mgmt.dao

import com.lms.app.config.db.PersistenceConfig
import com.lms.commons.models.Book
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
class BookManagementDaoTest {

    @Autowired
    private lateinit var bookManagementDao: BookManagementDao

    @Test
    fun insert() {
        Assertions.assertThat(bookManagementDao.insert(Book())).isEqualTo(1)
    }
}