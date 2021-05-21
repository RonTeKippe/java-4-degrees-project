package edu.cscc.degrees.api;

import edu.cscc.degrees.data.MenuCategoryRepository;
import edu.cscc.degrees.domain.MenuCategory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;



@RestController
@RequestMapping("/api/menu/categories")
public class DegreesPostController {

    private final MenuCategoryRepository menuCategoryRepository;

    public DegreesPostController(MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @PostMapping
    public ResponseEntity <MenuCategory> createMenuCategory(
            @RequestBody MenuCategory menuCategory, UriComponentsBuilder uriComponentsBuilder) {

        MenuCategory savedItem = menuCategoryRepository.save(menuCategory);

        UriComponents uriComponents =
                uriComponentsBuilder.path("/api/menu/categories/{id}")
                        .buildAndExpand(savedItem.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedItem, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public Iterable<MenuCategory> getAllItems () {
        return menuCategoryRepository.findAll();
    }

}
