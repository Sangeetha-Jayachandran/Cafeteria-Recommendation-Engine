package com.cafeteria.server.controllers;

import com.cafeteria.server.models.User;
import com.cafeteria.server.services.UserService;

public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public User authenticateUser(String username, String password) {
        return userService.authenticateUser(username, password);
    }

    public void addUser(User user) {
        userService.addUser(user);
    }
}
