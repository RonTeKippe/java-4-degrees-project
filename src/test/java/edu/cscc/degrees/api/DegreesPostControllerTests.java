package edu.cscc.degrees.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.degrees.data.MenuCategoryRepository;
import edu.cscc.degrees.domain.MenuCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class DegreesPostControllerTests {

    private static final String RESOURCE_URI = "/api/menu/categories";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MenuCategory testMenuCategory =
            new MenuCategory(0L, "category title", "category notes", 1);
    private static final MenuCategory savedMenuCategory =
            new MenuCategory(1L, "category title1", "category notes1", 2);

    @MockBean
    private MenuCategoryRepository menuCategoryRepository;

    @Test
    @DisplayName("T01 - POST accepts and returns menu category representation")
    public void postCreatesNewMenuCategory_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        when(menuCategoryRepository.save(any(MenuCategory.class))).thenReturn(savedMenuCategory);

        //doReturn(savedMenuCategory).when(menuCategoryRepository).save(any(MenuCategory.class));
        MvcResult result = mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testMenuCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(
                        "$.id").value(savedMenuCategory.getId()))
                .andExpect(jsonPath(
                        "$.categoryNotes").value(savedMenuCategory.getCategoryNotes()))
                .andExpect(jsonPath(
                        "$.categoryTitle").value(savedMenuCategory.getCategoryTitle()))
                .andExpect(
                        jsonPath("$.sortOrder").value(savedMenuCategory.getSortOrder()))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        assertEquals(String.format(
                "http://localhost/api/menu/categories/%d", savedMenuCategory.getId()),
                mockResponse.getHeader("Location"));

        verify(menuCategoryRepository, times(1)).save(any(MenuCategory.class));
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T02 - When no articles exist, GET returns an empty list")
    public void getReturnsEmptyListWhenNoCategories(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.findAll()).thenReturn(new ArrayList<MenuCategory>());
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(menuCategoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T03 - When one article exists, GET returns a list with it")
    public void getReturnsAListWithOneCategoryWhenOneExists(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.findAll()).
                thenReturn(Collections.singletonList(savedMenuCategory));
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(
                        "$.[0].id").value(savedMenuCategory.getId()))
                .andExpect(jsonPath(
                        "$.[0].categoryTitle").value(savedMenuCategory.getCategoryTitle()))
                .andExpect(jsonPath(
                        "$.[0].categoryNotes").value(savedMenuCategory.getCategoryNotes()))
                .andExpect(
                        jsonPath("$.[0].sortOrder").value(savedMenuCategory.getSortOrder()))
                .andExpect(status().isOk());
        verify(menuCategoryRepository, times(1)).findAll();
        verifyNoMoreInteractions(menuCategoryRepository);
    }
}