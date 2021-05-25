package edu.cscc.degrees.domain;


import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
public class MenuCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 1, max = 80,
        message = "Please enter a category title up to 80 characters in length")
    private String categoryTitle;
    private String categoryNotes;

    @NotNull(message = "sortOrder is required")
    private int sortOrder;



    public MenuCategory() {
    }

    public MenuCategory(long id, String categoryTitle, String categoryNotes, int sortOrder) {
        this.id = id;
        this.categoryTitle = categoryTitle;
        this.categoryNotes = categoryNotes;
        this.sortOrder = sortOrder;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryNotes() {
        return categoryNotes;
    }

    public void setCategoryNotes(String title) {
        this.categoryNotes = title;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}

