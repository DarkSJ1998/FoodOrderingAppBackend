package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "FLAT_BUIL_NUMBER")
    private String flatBuilNumber;

    @Column(name = "LOCALITY")
    private String locality;

    @Column(name = "CITY")
    private String city;

    @Column(name = "PINCODE")
    private String pincode;

    @Column(name = "STATE_ID")
    private int stateId;

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

    public String getFlatBuilNumber() {
        return flatBuilNumber;
    }

    public void setFlatBuilNumber(String flatBuilNumber) {
        this.flatBuilNumber = flatBuilNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }
}
