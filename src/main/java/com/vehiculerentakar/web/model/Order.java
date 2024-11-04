package com.vehiculerentakar.web.model;

import java.time.LocalDate;

public class Order {
    private String orderId;
    private String customerId;
    private String vehiculeId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Order(String orderId, String customerId, String vehiculeId, LocalDate startDate, LocalDate endDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.vehiculeId = vehiculeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Order() {

    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(String vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
