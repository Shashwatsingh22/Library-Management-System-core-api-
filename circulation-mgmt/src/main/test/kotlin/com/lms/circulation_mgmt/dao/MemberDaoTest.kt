package com.lms.circulation_mgmt.dao

import com.lms.app.config.db.PersistenceConfig
import com.lms.circulation_mgmt.models.MemberFilters
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
class MemberDaoTest {

    @Autowired
    private lateinit var memberDao: MemberDao

    @Test
    fun getMembers_forSearchText() {
        val memberFilters = MemberFilters()
        memberFilters.searchText = "test"
        Assertions.assertThat(memberDao.getMembers(memberFilters)).isNull()
    }
}