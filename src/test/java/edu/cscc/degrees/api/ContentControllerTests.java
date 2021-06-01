package edu.cscc.degrees.api;

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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContentControllerTests {

    @MockBean
    private MenuItemRepository menuItemRepository;

    private static final MenuCategory savedMenuCategory =
            new MenuCategory(0L, "category title", "category notes", 1);
    private static final MenuItem savedMenuItem =
            new MenuItem (0L, savedMenuCategory, "item name", "item decription", "item price", 1);

    /**@Test
    @DisplayName("T01 - Get summary articles returns data ")
    public void test_01(@Autowired MockMvc mockMvc) throws Exception {
        when(menuItemRepository.getAllMenuItemsOmittingContent()).
                thenReturn(Collections.singletonList(savedMenuItem));
        mockMvc.perform(get("/api/summary/articles"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(menuItemRepository, times(1))
                .getAllMenuItmesOmittingContent();
        verifyNoMoreInteractions(menuItemRepository);
    } **/
}
