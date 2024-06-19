package com.cafeteria.server.models;

import java.util.Date;

public class Order {
    private int orderId;
    private int userId;
    private int itemId;
    private Date orderDate;

    public Order(int userId, int itemId, Date orderDate) {
        this.userId = userId;
        this.itemId = itemId;
        this.orderDate = orderDate;
    }

    public Order(int orderId, int userId, int itemId, Date orderDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.itemId = itemId;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getUserId() {
        return userId;
    }

    public int getItemId() {
        return itemId;
    }

    public Date getOrderDate() {
        return orderDate;
    }
}
