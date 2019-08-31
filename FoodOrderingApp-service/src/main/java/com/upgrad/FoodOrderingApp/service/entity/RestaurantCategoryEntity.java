package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "RESTAURANT_CATEGORY")
public class RestaurantCategoryEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "RESTAURANT_ID")
    @NotNull
    private long restaurantId;

    @Column(name = "CATEGORY_ID")
    @NotNull
    private long categoryId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "RestaurantCategoryEntity{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", categoryId=" + categoryId +
                '}';
    }
}
