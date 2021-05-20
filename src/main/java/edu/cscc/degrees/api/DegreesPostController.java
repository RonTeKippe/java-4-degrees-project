package edu.cscc.degrees.api;

import edu.cscc.degrees.data.MenuCategoryRepository;
import edu.cscc.degrees.domain.MenuCategory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/menu/categories")
public class DegreesPostController {

    private final MenuCategoryRepository menuCategoryRepository;

    public DegreesPostController(MenuCategoryRepository menuCategoryRepository) {
        this.menuCategoryRepository = menuCategoryRepository;
    }

    @PostMapping
    public ResponseEntity <MenuCategory> createBlogPost(
            @RequestBody MenuCategory menuCategory, UriComponentsBuilder uriComponentsBuilder) {
        menuCategory.setDatePosted(LocalDateTime.now());
        MenuCategory savedItem = menuCategoryRepository.save(menuCategory);

        UriComponents uriComponents =
                uriComponentsBuilder.path("/api/menu/categories/{id}")
                        .buildAndExpand(menuCategory.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(menuCategory, headers, HttpStatus.CREATED);
    }

    /**@PostMapping
    public ResponseEntity<BlogPost> createBlogEntry(
            @RequestBody BlogPost blogPost, UriComponentsBuilder uriComponentsBuilder) {
        BlogPost savedItem = blogPostRepository.save(blogPost);
        UriComponents uriComponents =
                uriComponentsBuilder.path("/api/articles/{id}")
                        .buildAndExpand(savedItem.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uriComponents.toUri().toString());
        return new ResponseEntity<>(savedItem, headers, HttpStatus.CREATED);
    }**/



}
