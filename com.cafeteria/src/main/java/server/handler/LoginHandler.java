package server.handler;

import model.User;
import controller.AuthController;
import util.Logger;

import java.io.PrintWriter;

public class LoginHandler {
    private static AuthController authController = new AuthController();

    public static String handleLogin(String command, PrintWriter writer) {
        String[] parts = command.split(" ");
        if (parts.length != 3) {
            writer.println("Invalid login command. Use: LOGIN <username> <password>");
            return null;
        }
        String username = parts[1];
        String password = parts[2];
        User user = authController.authenticate(username, password);
        if (user != null) {
            writer.println("Login successful. Welcome " + username + "!");
            Logger.log(username + " logged in.");
            return username;
        } else {
            writer.println("Login failed. Invalid username or password.");
            return null;
        }
    }
}
