package server.handler;

import model.User;
import controller.AuthController;
import util.Logger;

import java.io.PrintWriter;

public class LoginHandler {
    private static AuthController authController = new AuthController();

    public static User handleLogin(String command, PrintWriter writer) {
        String[] parts = command.split(" ");
        if (parts.length != 4) {
            writer.println("Invalid login command. Use: LOGIN <username> <password> <role>");
            Logger.log("Invalid login command format: " + command);
            return null;
        }
        String username = parts[1].trim();
        String password = parts[2].trim();
        String role = parts[3].trim().toUpperCase();

        User user = authController.authenticate(username, password, role);
        if (user != null) {
            writer.println("Login successful. Welcome " + username + "!");
            Logger.log(username + " logged in as " + role + ".");
            return user;
        } else {
            writer.println("Invalid username, password, or role.");
            Logger.log("Login failed for user: " + username + " with role: " + role);
            return null;
        }
    }
}
