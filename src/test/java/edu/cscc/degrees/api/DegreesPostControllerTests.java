package edu.cscc.degrees.api;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class DegreesPostControllerTests {

    private static final String RESOURCE_URI = "/api/menu/catergories";

    @Test
    void postReturnsStatusOfCreated(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(post(RESOURCE_URI))
                .andExpect(status().isCreated());
    }

    @Test
    void postReturnsLocationOfDegreesPostCreated(@Autowired MockMvc mockMvc) throws Exception {

        MvcResult result = mockMvc.perform(post(RESOURCE_URI))
                .andReturn();

        MockHttpServletResponse mockResponse = result.getResponse();

        assertEquals("http://localhost/api/menu/categories/1", mockResponse.getHeader("Location"));
    }
}
