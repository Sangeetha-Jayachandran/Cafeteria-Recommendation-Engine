package server;

import server.handler.AdminHandler;
import server.handler.ChefHandler;
import server.handler.EmployeeHandler;
import server.handler.LoginHandler;
import util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import model.User;

public class ServerThread extends Thread {
    private Socket socket;
    private String username;
    private String role;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            String text;
            while ((text = reader.readLine()) != null) {
                System.out.println("Received: " + text);
                Logger.log("Received from " + (username != null ? username : "unknown user") + ": " + text);

                if (text.startsWith("LOGIN")) {
                    User user = LoginHandler.handleLogin(text, writer);
                    if (user != null) {
                        username = user.getUsername();
                        role = user.getRoleName();
                        Logger.log("User logged in: " + username + " with role " + role);
                        if ("EMPLOYEE".equals(role)) {
                            new EmployeeHandler().handle("EMPLOYEE VIEW_NOTIFICATION", writer);
                        }
                    }
                } else if (text.equals("LOGOUT")) {
                    writer.println("END_OF_RESPONSE");
                    writer.flush();
                    Logger.log("User logged out: " + username);
                    username = null;
                    role = null;
                } else if (username != null) {
                    handleUserCommand(text, writer);
                } else {
                    writer.println("Please log in first.");
                }
            }
            socket.close();
            Logger.log("Client disconnected: " + (username != null ? username : "unknown user"));
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            Logger.log("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleUserCommand(String text, PrintWriter writer) {
        if ("ADMIN".equals(role)) {
            new AdminHandler().handle(text, writer);
        } else if ("CHEF".equals(role)) {
            new ChefHandler().handle(text, writer);
        } else if ("EMPLOYEE".equals(role)) {
            new EmployeeHandler().handle(text, writer);
        } else {
            writer.println("Invalid command.");
        }
    }
}
