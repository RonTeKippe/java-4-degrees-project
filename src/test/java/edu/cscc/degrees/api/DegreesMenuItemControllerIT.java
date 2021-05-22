package edu.cscc.degrees.api;

import edu.cscc.degrees.domain.MenuCategory;
import edu.cscc.degrees.domain.MenuItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class DegreesMenuItemControllerIT {

     @LocalServerPort
        private int localServerPort;
        @Autowired
        private TestRestTemplate restTemplate;
        private static final String RESOURCE_URI = "http://localhost:%d/api/menu/categories";
        private static final MenuCategory testMenuCategory =
                new MenuCategory(0L, "category title", "category notes", 1);
        private static final MenuItem testMenuItem =
                new MenuItem(0L, testMenuCategory, "item name", "item decription", "item price", 1);

        @Test
        @DisplayName("T01 - POSTed location includes server port when created")
        public void test_01() {
            ResponseEntity<MenuItem> responseEntity =
                    this.restTemplate.postForEntity(String.format(RESOURCE_URI,
                            localServerPort), testMenuItem, MenuItem.class);
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            assertEquals(localServerPort,
                    responseEntity.getHeaders().getLocation().getPort());
        }

        @Test
        @DisplayName("T02 - POST generates nonzero ID")
        public void test_02() {
            ResponseEntity<MenuItem> responseEntity =
                    this.restTemplate.postForEntity(String.format(RESOURCE_URI, localServerPort),
                            testMenuItem, MenuItem.class);
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            MenuItem menuItemReturned = responseEntity.getBody();
            assertNotEquals(testMenuItem.getId(), menuItemReturned.getId());
            assertEquals(String.format(RESOURCE_URI + "/%d", localServerPort,
                    menuItemReturned.getId()), responseEntity.getHeaders().getLocation().toString());
        }

    }
