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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.doReturn;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

public class DegreesMenuCategoryControllerTests {

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
    @DisplayName("T02 - When no category exist, GET returns an empty list")
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
    @DisplayName("T03 - When one category exists, GET returns a list with it")
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

    @Test
    @DisplayName("T04 - Requested category does not exist so GET returns 404")
    public void requestedCategoryDoesNotExitSoGetReturns404(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        mockMvc.perform(get(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(menuCategoryRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T05 - Requested categorty exists so GET returns it in a list")
    public void requestedCategoryExitstSoGetReturnsItInAList(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedMenuCategory));
        mockMvc.perform(get(RESOURCE_URI + "/1"))
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
        verify(menuCategoryRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T06 - Category to be updated does not exist so PUT returns 404")
    public void putReturns404WhenCategoryDoesNotExist(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.existsById(10L)).thenReturn(false);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new MenuCategory(10L,"category title", "category notes", 1))))
                .andExpect(status().isNotFound());
        verify(menuCategoryRepository, never()).save(any(MenuCategory.class));
        verify(menuCategoryRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T07 - Category to be updated exists so PUT saves new copy")
    public void categoryExistsSoPutSaveANewCopy(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.existsById(10L)).thenReturn(true);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new MenuCategory(10L,"category title", "category notes", 1))))
                .andExpect(status().isNoContent());
        verify(menuCategoryRepository, times(1)).save(any(MenuCategory.class));
        verify(menuCategoryRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T08 - ID in PUT URL not equal to one in request body")
    public void idInPutNotEqualToRequestBodyShouldReturn409(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(put(RESOURCE_URI + "/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new MenuCategory(10L,"category title", "category notes", 1))))
                .andExpect(status().isConflict());
        verify(menuCategoryRepository, never()).save(any(MenuCategory.class));
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T09 - Category to be removed does not exist so DELETE returns 404")
    public void deleteReturns404WhenCategoryDoesNotExist(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.findById(1L))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(menuCategoryRepository, never()).delete(any(MenuCategory.class));
        verify(menuCategoryRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T10 - Category to be removed exists so DELETE deletes it")
    public void deleteMenuCategoryWhenItExists(@Autowired MockMvc mockMvc) throws Exception {
        when(menuCategoryRepository.findById(1L))
                .thenReturn(Optional.of(savedMenuCategory));
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNoContent());
        verify(menuCategoryRepository, times(1)).delete(refEq(savedMenuCategory));
        verify(menuCategoryRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T11 - POST returns 400 if required properties are not set")
    public void PostReturns400IfMenuCategoryIsMissingFields(@Autowired MockMvc mockMvc) throws Exception {

        mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new MenuCategory())))
                .andExpect(status().isBadRequest());
        verify(menuCategoryRepository, never()).save(any(MenuCategory.class));
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T12 - POST returns null value error messages")
    public void postReturnsNullValueErrorMessages(@Autowired MockMvc mockMvc) throws Exception {

        mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new MenuCategory())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.categoryTitle").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.sortOrder").value("sortOrder is required"));
        verify(menuCategoryRepository, never()).save(any(MenuCategory.class));
        verifyNoMoreInteractions(menuCategoryRepository);
    }

    @Test
    @DisplayName("T12b - POST returns returns the correct error messages")
    public void postReturnsCorrectErrorMessages(@Autowired MockMvc mockMvc) throws Exception {

        mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new MenuCategory(0L,"", "category notes", 1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.categoryTitle").value(
                        "Please enter a category title up to 80 characters in length"));
        verify(menuCategoryRepository, never()).save(any(MenuCategory.class));
        verifyNoMoreInteractions(menuCategoryRepository);
    }


}