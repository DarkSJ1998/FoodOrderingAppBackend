package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "CATEGORY_ITEM")
public class CategoryItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ITEM_ID")
    @NotNull
    private long itemId;

    @Column(name = "CATEGORY_ID")
    @NotNull
    private long categoryId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "CategoryItemEntity{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", categoryId=" + categoryId +
                '}';
    }
}
