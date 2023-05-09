package com.lms.app.controller

import com.lms.commons.models.BuildInfo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

/**
 * @author Shashwat Singh
 */

class PingControllerTest {

    private lateinit var mockMvc: MockMvc

    @InjectMocks
    private lateinit var pingController: PingController

    @BeforeEach
    fun setUp() {
        pingController = PingController(BuildInfo("1.1","test"))
        this.mockMvc = MockMvcBuilders.standaloneSetup(pingController).build()
    }

    @Test
    fun getPing() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/ping")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
    }
}