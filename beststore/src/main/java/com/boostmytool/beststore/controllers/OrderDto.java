package com.boostmytool.beststore.controllers;

import java.util.List;

public class OrderDto {

    private String customerName;
    private String customerEmail;
    private String customerAddress;
    private String customerPhone;
    private List<Long> productIds; // Lista e ID-ve të produkteve në porosi

    // Konstruktori pa parametra
    public OrderDto() {
    }

    // Konstruktori me parametra
    public OrderDto(String customerName, String customerEmail, String customerAddress, String customerPhone, List<Long> productIds) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.productIds = productIds;
    }

    // Getters dhe Setters
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", productIds=" + productIds +
                '}';
    }
}
