package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "RESTAURANT")
public class RestaurantEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "RESTAURANT_NAME")
    @NotNull
    private String restaurantName;

    @Column(name = "PHOTO_URL")
    private String photoUrl;

    @Column(name = "CUSTOMER_RATING")
    @NotNull
    private float customerRating;

    @Column(name = "AVERAGE_PRICE_FOR_TWO")
    @NotNull
    private int averagePriceForTwo;

    @Column(name = "NUMBER_OF_CUSTOMERS_RATED")
    @NotNull
    private int numberOfCustomersRated;

    @Column(name = "ADDRESS_ID")
    @NotNull
    private int addressId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public float getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(float customerRating) {
        this.customerRating = customerRating;
    }

    public int getAveragePriceForTwo() {
        return averagePriceForTwo;
    }

    public void setAveragePriceForTwo(int averagePriceForTwo) {
        this.averagePriceForTwo = averagePriceForTwo;
    }

    public int getNumberOfCustomersRated() {
        return numberOfCustomersRated;
    }

    public void setNumberOfCustomersRated(int numberOfCustomersRated) {
        this.numberOfCustomersRated = numberOfCustomersRated;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
