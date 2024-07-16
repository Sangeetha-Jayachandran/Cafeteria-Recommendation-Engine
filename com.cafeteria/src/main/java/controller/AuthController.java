package controller;

import service.AuthService;
import model.User;

public class AuthController {
    private final AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    public User authenticate(String username, String password, String role) {
        return authService.authenticate(username, password, role);
    }
}
