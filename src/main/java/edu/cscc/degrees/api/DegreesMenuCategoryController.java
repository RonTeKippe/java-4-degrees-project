package edu.cscc.degrees.api;

import edu.cscc.degrees.data.MenuCategoryRepository;
import edu.cscc.degrees.domain.MenuCategory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;


@RestController
@RequestMapping("/api/menu/categories")
public class DegreesMenuCategoryController {

    private final MenuCategoryRepository menuCategoryRepository;

    public DegreesMenuCategoryController(MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @PostMapping
    public ResponseEntity <MenuCategory> createMenuCategory(
            @Valid @RequestBody MenuCategory menuCategory, UriComponentsBuilder uriComponentsBuilder) {

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

    @GetMapping("{id}")
    public ResponseEntity<Iterable<MenuCategory>> getItemById(
            @PathVariable Long id) {
        Optional<MenuCategory> searchResult = menuCategoryRepository.findById(id);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    Collections.singletonList(searchResult.get()),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity<MenuCategory> updateMenuCategoryEntry(@PathVariable Long id,
                                                        @RequestBody MenuCategory menuCategory) {
        if (menuCategory.getId() != id) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (menuCategoryRepository.existsById(id)) {
            menuCategoryRepository.save(menuCategory);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MenuCategory> deleteMenuCategoryEntryById(@PathVariable Long id) {
        Optional<MenuCategory> blogEntry = menuCategoryRepository.findById(id);
        if (blogEntry.isPresent()) {
            menuCategoryRepository.delete(blogEntry.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
