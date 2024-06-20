package com.cafeteria.server.controllers;

import com.cafeteria.server.models.User;
import com.cafeteria.server.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class UserController {
    private final UserService userService;
    private final List<User> users;

    public UserController() {
        this.userService = new UserService();
        this.users = new ArrayList<User>();
    }

    public User authenticateUser(String username, String password) {
        return userService.authenticateUser(username, password);
    }

    public void addUser(User user) {
        userService.addUser(user);
    }
    
    public List<User> getUsersByRole(String role) {
        List<User> usersByRole = new ArrayList<User>();
        for (User user : users) {
            if (user.getRole().equals(role)) {
                usersByRole.add(user);
            }
        }
        return usersByRole;
    }
}
