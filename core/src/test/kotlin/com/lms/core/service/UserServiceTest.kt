package com.lms.core.service

import com.lms.commons.models.User
import com.lms.core.dao.UserDao
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * @author Shashwat Singh
 */

class UserServiceTest {

    @InjectMocks
    private lateinit var userService: UserService

    @Mock
    private lateinit var userDao: UserDao

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun get() {
        Assertions.assertThat(userService.get(User("test"))).isNull()
    }
}