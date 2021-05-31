package edu.cscc.degrees.data;

import edu.cscc.degrees.domain.MenuCategory;
import edu.cscc.degrees.domain.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class degreesMenuJdbcTemplateRepository {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public List<MenuCategory> getAllMenuCategoriesOmittingContent() {
        return jdbcTemplate.query(
                "select id, title, category_title sort_order" +
                        "from menu_category order by sort_order desc",
                BeanPropertyRowMapper.newInstance(MenuCategory.class));
    }

    /**public List<Category> getCategoryList() {
        return jdbcTemplate.query(
                "select distinct category from menu_category order by category",
                (resultsRow, rowNum) -> {
                    Category category = new Category();
                    category.setId((long) rowNum);
                    category.setCategoryName(resultsRow.getString("category"));
                    return category;
                });
    }**/
    public List<MenuItem> getAllMenuItemsOmittingContent() {
        return jdbcTemplate.query(
                "select id, item_name, sort_order" +
                        "from menu_item order by sort_order desc",
                BeanPropertyRowMapper.newInstance(MenuItem.class));
    }

}
