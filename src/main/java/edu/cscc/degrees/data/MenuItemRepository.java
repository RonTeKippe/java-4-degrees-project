package edu.cscc.degrees.data;

import edu.cscc.degrees.domain.MenuItem;
import org.springframework.data.repository.CrudRepository;

public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
}
