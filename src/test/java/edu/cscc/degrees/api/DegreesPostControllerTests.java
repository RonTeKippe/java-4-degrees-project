package edu.cscc.degrees.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.degrees.domain.MenuCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class DegreesPostControllerTests {

    private static final String RESOURCE_URI = "/api/menu/categories";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MenuCategory testMenuCategory =
            new MenuCategory(0L, "category", null, "title", "content");

    @Test
    @DisplayName("T01 - POST accepts and returns menu category representation")
    public void postCreatesNewMenuCategory_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        MvcResult result = mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testMenuCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(
                        "$.id").value(testMenuCategory.getId()))
                .andExpect(jsonPath(
                        "$.title").value(testMenuCategory.getTitle()))
                .andExpect(jsonPath(
                        "$.category").value(testMenuCategory.getCategory()))
                .andExpect(
                        jsonPath("$.content").value(testMenuCategory.getContent()))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        assertEquals(String.format(
                "http://localhost/api/menu/categories/%d", testMenuCategory.getId()),
                mockResponse.getHeader("Location"));
    }
}
