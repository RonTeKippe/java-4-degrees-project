package edu.cscc.degrees.data;


import edu.cscc.degrees.domain.MenuCategory;
import edu.cscc.degrees.domain.MenuItem;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface MenuItemRepository extends PagingAndSortingRepository<MenuItem, Long> {

    List<MenuItem>  findAllByMenuCategory(MenuCategory menuCategory, Pageable pageable);
}
