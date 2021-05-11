package edu.cscc.degrees.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu/categories")
public class DegreesPostController {

    @PostMapping
    public ResponseEntity createBlogPost() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "http://localhost/api/menu/categories/1");
        return new ResponseEntity("", headers,HttpStatus.CREATED);
    }

}
