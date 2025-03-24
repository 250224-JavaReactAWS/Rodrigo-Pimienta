package com.revature.models;


public class UserAddress {
    // FIELDS

    private int addressId;
    private int userId;
    private String country;
    private String state;
    private String city;
    private String street;
    private String houseNumber;
    private String postalCode;
    private boolean active;


    // Constructors (overloading) Polymorphism
    public UserAddress() {
    }

    public UserAddress(int addressId) {
        this.addressId = addressId;
    }

    public UserAddress(int addressId, int userId, String country, String city, String state, String street, String houseNumber, String postalCode) {
        this.addressId = addressId;
        this.userId = userId;
        this.country = country;
        this.state = state;
        this.street = street;
        this.city = city;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
    }

    public UserAddress(int addressId, int userId, String country, String state, String city, String street, String houseNumber, String postalCode, boolean active) {
        this.addressId = addressId;
        this.userId = userId;
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.active = active;
    }

    // Mutators (Getters and Setters)

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
