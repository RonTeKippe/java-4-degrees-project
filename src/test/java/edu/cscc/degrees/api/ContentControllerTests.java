package edu.cscc.degrees.api;

import edu.cscc.degrees.data.MenuCategoryRepository;
import edu.cscc.degrees.data.MenuItemRepository;
import edu.cscc.degrees.domain.MenuCategory;
import edu.cscc.degrees.domain.MenuItem;
import edu.cscc.degrees.domain.MenuItemList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContentControllerTests {

    @MockBean
    private MenuItemRepository menuItemRepository;
    @MockBean
    private MenuCategoryRepository menuCategoryRepository;

    private static final MenuCategory savedMenuCategory =
            new MenuCategory(0L, "category title", "category notes", 1);
    private static final MenuItem savedMenuItem =
            new MenuItem (0L, savedMenuCategory, "item name", "item decription", "item price", 1);

    private static List<MenuItem> list = new ArrayList<>();


    private static MenuItemList savedMenuItemList = new MenuItemList(savedMenuCategory, list);


    @Test
    @DisplayName("T01 - get All Items In A Category returns data ")
    public void test_01(@Autowired MockMvc mockMvc) throws Exception {
        //list.add(savedMenuItem);
        //ContentController contentController = new ContentController(menuCategoryRepository, menuItemRepository);
        when(menuCategoryRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(savedMenuCategory));
        when(menuItemRepository.findByMenuCategoryOrderBySortOrderAscItemNameAsc(savedMenuCategory)).
                thenReturn(Collections.singletonList(savedMenuItem));
        mockMvc.perform(get("/public/api/menus"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(menuCategoryRepository, times(1))
                .findAll(any(Sort.class));
        verifyNoMoreInteractions(menuCategoryRepository);
        verify(menuItemRepository, times(1))
                .findByMenuCategoryOrderBySortOrderAscItemNameAsc(savedMenuCategory);
        verifyNoMoreInteractions(menuItemRepository);
    }
}
