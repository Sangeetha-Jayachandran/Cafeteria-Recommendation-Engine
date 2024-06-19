package com.cafeteria.server.controllers;

import com.cafeteria.server.models.Order;
import com.cafeteria.server.services.OrderService;

import java.util.List;

public class OrderController {
    private final OrderService orderService;

    public OrderController() {
        this.orderService = new OrderService();
    }

    public void addOrder(Order order) {
        orderService.addOrder(order);
    }

    public List<Order> getUserOrders(int userId) {
        return orderService.getUserOrders(userId);
    }

    public boolean hasOrdered(int userId, int itemId) {
        return orderService.hasOrdered(userId, itemId);
    }
}
