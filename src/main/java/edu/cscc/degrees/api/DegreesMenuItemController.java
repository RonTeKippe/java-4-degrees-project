package edu.cscc.degrees.api;


import edu.cscc.degrees.data.MenuItemRepository;

import edu.cscc.degrees.domain.MenuCategory;
import edu.cscc.degrees.domain.MenuItem;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu/items")
public class DegreesMenuItemController {

    private final MenuItemRepository menuItemRepository;


    public DegreesMenuItemController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @PostMapping
    public ResponseEntity<MenuItem> createMenuItem(
            @Valid @RequestBody MenuItem menuItem, UriComponentsBuilder uriComponentsBuilder) {

        MenuItem savedItem = menuItemRepository.save(menuItem);

        UriComponents uriComponents =
                uriComponentsBuilder.path("/api/menu/items/{id}")
                        .buildAndExpand(savedItem.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedItem, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public Iterable<MenuItem> getAllItems () {
        return menuItemRepository.findAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Iterable<MenuItem>> getItemById(
            @PathVariable Long id) {
        Optional<MenuItem> searchResult = menuItemRepository.findById(id);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    Collections.singletonList(searchResult.get()),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**@GetMapping("{menuCategory}")
    public ResponseEntity<Iterable<MenuItem>> getAllItemsInACategory(
            @PathVariable MenuCategory menuCategory) {
        Pageable allItemsInACategory = new PageRequest(0, 5);
        Optional<MenuItem> searchResult = menuItemRepository.findAllByMenuCategory(menuCategory, allItemsInACategory);
        if (searchResult.isPresent()) {
            return new ResponseEntity<>(
                    searchResult, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }**/


    @PutMapping("{id}")
    public ResponseEntity<MenuItem> updateMenuItemEntry(@PathVariable Long id,
                                                        @Valid @RequestBody MenuItem menuItem) {
        if (menuItem.getId() != id) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (menuItemRepository.existsById(id)) {
            menuItemRepository.save(menuItem);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MenuItem> deleteMenuItemEntryById(@PathVariable Long id) {
        Optional<MenuItem> blogEntry = menuItemRepository.findById(id);
        if (blogEntry.isPresent()) {
            menuItemRepository.delete(blogEntry.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
