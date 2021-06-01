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

import java.util.List;
import java.util.Optional;

@RestController
public class ContentController {

    MenuCategoryRepository menuCategoryRepository;

    MenuItemRepository menuItemRepository;

    public ContentController(MenuCategoryRepository menuCategoryRepository, MenuItemRepository menuItemRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/public/api/menus")
    public ResponseEntity<Iterable<MenuItemList>> getAllItemsInACategory(
        {
          // MenuCategoryRepository.

                List<MenuItemList> searchResult = menuItemRepository.findByMenuCategoryOrderBySortOrderAscNameAsc();
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    searchResult, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<MenuCategory> menuCategoryList = MenuCategoryRepository.class.

}
