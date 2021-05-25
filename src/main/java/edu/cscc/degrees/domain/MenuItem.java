package edu.cscc.degrees.domain;



import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "menuCategory is required")
    private MenuCategory menuCategory;

    @NotNull
    @Size(min = 1, max = 80,
            message = "Please enter a name up to 80 characters in length")
    private String itemName;
    private String itemDescription;

    @NotNull
    @Size(min = 1, max = 20,
            message = "Please enter a price up to 80 characters in length")
    private String itemPrice;

    @NotNull(message = "sortOrder is required")
    private Integer sortOrder;

    public MenuItem() {
    }

    public MenuItem(Long id, MenuCategory menuCategory, String itemName, String itemDescription, String itemPrice, Integer sortOrder) {
        this.id = id;
        this.menuCategory = menuCategory;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MenuCategory getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}
