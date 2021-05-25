package edu.cscc.degrees.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cscc.degrees.data.MenuItemRepository;
import edu.cscc.degrees.domain.MenuCategory;
import edu.cscc.degrees.domain.MenuItem;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc

public class DegreesMenuItemControllerTests {

    private static final String RESOURCE_URI = "/api/menu/items";
    private final ObjectMapper mapper = new ObjectMapper();
    private static final MenuCategory testMenuCategory =
            new MenuCategory(0L, "category title", "category notes", 1);
    private static final MenuItem testMenuItem =
            new MenuItem (0L, testMenuCategory, "item name", "item decription", "item price", 1);
    private static final MenuItem savedMenuItem =
            new MenuItem(1L, testMenuCategory, "item name1", "item decription1", "item price1", 2);

    @MockBean
    private MenuItemRepository menuItemRepository;

    @Test
    @DisplayName("T01 - POST accepts and returns menu item representation")
    public void postCreatesNewMenuItem_Test(@Autowired MockMvc mockMvc)
            throws Exception {
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedMenuItem);

        //doReturn(savedMenuCategory).when(menuCategoryRepository).save(any(MenuCategory.class));
        MvcResult result = mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testMenuItem)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath(
                        "$.id").value(savedMenuItem.getId()))
                .andExpect(jsonPath(
                        "$.menuCategory.id").value(savedMenuItem.getMenuCategory().getId()))
                .andExpect(jsonPath(
                        "$.itemName").value(savedMenuItem.getItemName()))
                .andExpect(jsonPath(
                        "$.itemDescription").value(savedMenuItem.getItemDescription()))
                .andExpect(jsonPath(
                        "$.itemPrice").value(savedMenuItem.getItemPrice()))
                .andExpect(
                        jsonPath("$.sortOrder").value(savedMenuItem.getSortOrder()))
                .andReturn();
        MockHttpServletResponse mockResponse = result.getResponse();
        assertEquals(String.format(
                "http://localhost/api/menu/items/%d", savedMenuItem.getId()),
                mockResponse.getHeader("Location"));

        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T02 - When no menu item exist, GET returns an empty list")
    public void getReturnsEmptyListWhenNoCategories(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.findAll()).thenReturn(new ArrayList<MenuItem>());
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(menuItemRepository, times(1)).findAll();
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T03 - When one menu item exists, GET returns a list with it")
    public void getReturnsAListWithOneItemWhenOneExists(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.findAll()).
                thenReturn(Collections.singletonList(savedMenuItem));
        mockMvc.perform(get(RESOURCE_URI))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(
                        "$.[0].id").value(savedMenuItem.getId()))
                .andExpect(jsonPath(
                        "$.[0].menuCategory.id").value(savedMenuItem.getMenuCategory().getId()))
                .andExpect(jsonPath(
                        "$.[0].itemName").value(savedMenuItem.getItemName()))
                .andExpect(jsonPath(
                        "$.[0].itemDescription").value(savedMenuItem.getItemDescription()))
                .andExpect(jsonPath(
                        "$.[0].itemPrice").value(savedMenuItem.getItemPrice()))
                .andExpect(
                        jsonPath("$.[0].sortOrder").value(savedMenuItem.getSortOrder()))
                .andExpect(status().isOk());
        verify(menuItemRepository, times(1)).findAll();
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T04 - Requested item does not exist so GET returns 404")
    public void requestedItemDoesNotExitSoGetReturns404(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        mockMvc.perform(get(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(menuItemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T05 - Requested item exists so GET returns it in a list")
    public void requestedItemExitstSoGetReturnsItInAList(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.findById(anyLong()))
                .thenReturn(Optional.of(savedMenuItem));
        mockMvc.perform(get(RESOURCE_URI + "/1"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(
                "$.[0].id").value(savedMenuItem.getId()))
                .andExpect(jsonPath(
                        "$.[0].menuCategory.id").value(savedMenuItem.getMenuCategory().getId()))
                .andExpect(jsonPath(
                        "$.[0].itemName").value(savedMenuItem.getItemName()))
                .andExpect(jsonPath(
                        "$.[0].itemDescription").value(savedMenuItem.getItemDescription()))
                .andExpect(jsonPath(
                        "$.[0].itemPrice").value(savedMenuItem.getItemPrice()))
                .andExpect(
                        jsonPath("$.[0].sortOrder").value(savedMenuItem.getSortOrder()))
                .andExpect(status().isOk());
        verify(menuItemRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T06 - Item to be updated does not exist so PUT returns 404")
    public void putReturns404WhenItemDoesNotExist(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.existsById(10L)).thenReturn(false);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new MenuItem(10L,testMenuCategory, "item name", "item decription", "item price", 1))))
                .andExpect(status().isNotFound());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
        verify(menuItemRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T07 - Item to be updated exists so PUT saves new copy")
    public void itemExistsSoPutSaveANewCopy(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.existsById(10L)).thenReturn(true);
        mockMvc.perform(put(RESOURCE_URI + "/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new MenuItem(10L,testMenuCategory, "item name", "item decription", "item price", 1))))
                .andExpect(status().isNoContent());
        verify(menuItemRepository, times(1)).save(any(MenuItem.class));
        verify(menuItemRepository, times(1)).existsById(10L);
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T08 - ID in PUT URL not equal to one in request body")
    public void idInPutNotEqualToRequestBodyShouldReturn409(@Autowired MockMvc mockMvc) throws Exception {
        mockMvc.perform(put(RESOURCE_URI + "/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(
                        new MenuItem(10L,testMenuCategory, "item name", "item decription", "item price", 1))))
                .andExpect(status().isConflict());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T09 - Item to be removed does not exist so DELETE returns 404")
    public void deleteReturns404WhenItemDoesNotExist(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.findById(1L))
                .thenReturn(Optional.empty());
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNotFound());
        verify(menuItemRepository, never()).delete(any(MenuItem.class));
        verify(menuItemRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T10 - Item to be removed exists so DELETE deletes it")
    public void deleteMenuItemWhenItExists(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.findById(1L))
                .thenReturn(Optional.of(savedMenuItem));
        mockMvc.perform(delete(RESOURCE_URI + "/1"))
                .andExpect(status().isNoContent());
        verify(menuItemRepository, times(1)).delete(refEq(savedMenuItem));
        verify(menuItemRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T11 - POST returns 400 if required properties are not set")
    public void PostReturns400IfMenuItemIsMissingFields(@Autowired MockMvc mockMvc) throws Exception {

        mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new MenuItem())))
                .andExpect(status().isBadRequest());
        verify(menuItemRepository, never()).save(any(MenuItem.class));
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T12 - POST returns null value error messages")
    public void postReturnsNullValueErrorMessages(@Autowired MockMvc mockMvc) throws Exception {

        mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new MenuItem())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.menuCategory").value("menuCategory is required"))
                .andExpect(jsonPath("$.fieldErrors.itemName").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.itemPrice").value("must not be null"))
                .andExpect(jsonPath("$.fieldErrors.sortOrder").value("sortOrder is required"));
        verify(menuItemRepository, never()).save(any(MenuItem.class));
        verifyNoMoreInteractions(menuItemRepository);
    }

    @Test
    @DisplayName("T12b - POST returns returns the correct error messages")
    public void postReturnsCorrectErrorMessages(@Autowired MockMvc mockMvc) throws Exception {

        mockMvc.perform(post(RESOURCE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new MenuItem(10L,testMenuCategory, "", "item decription", "", 1))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.itemName").value(
                        "Please enter a name up to 80 characters in length"))
                .andExpect(jsonPath("$.fieldErrors.itemPrice").value(
                        "Please enter a price up to 80 characters in length"));
        verify(menuItemRepository, never()).save(any(MenuItem.class));
        verifyNoMoreInteractions(menuItemRepository);
    }



}
