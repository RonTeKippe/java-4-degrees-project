package edu.cscc.degrees.api;


import edu.cscc.degrees.data.MenuCategoryRepository;
import edu.cscc.degrees.data.MenuItemRepository;
import edu.cscc.degrees.domain.MenuCategory;
import edu.cscc.degrees.domain.MenuItem;
import edu.cscc.degrees.domain.MenuItemList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class ContentController {

    MenuCategoryRepository menuCategoryRepository;

    MenuItemRepository menuItemRepository;

    public ContentController(MenuCategoryRepository menuCategoryRepository, MenuItemRepository menuItemRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/public/api/menus")
    public Iterable<MenuItemList> getAllItemsInACategory()
        {
          Iterable<MenuCategory> menuCategories = menuCategoryRepository.findAll(Sort.by("sortOrder", "categoryTitle"));

          List<MenuItemList>
                  searchResult = new ArrayList<>();
          menuCategories.forEach(menuCategory ->  {
              MenuItemList menuItemList = new MenuItemList();
              menuItemList.setMenuCategory(menuCategory);
              menuItemList.setMenuItemList(menuItemRepository.findByMenuCategoryOrderBySortOrderAscItemNameAsc(menuCategory));
              searchResult.add(menuItemList);
          });
            return searchResult;
        }


}
