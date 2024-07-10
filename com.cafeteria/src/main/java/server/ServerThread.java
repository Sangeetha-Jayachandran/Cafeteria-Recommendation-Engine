package server;

import server.handler.AdminHandler;
import server.handler.ChefHandler;
import server.handler.EmployeeHandler;
import server.handler.LoginHandler;
import util.Logger;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private String username;
    private int userId;
    private BufferedReader reader;
    private PrintWriter writer;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            String text;
            while ((text = reader.readLine()) != null) {
                System.out.println("Received: " + text);
                Logger.log("Received from " + (username != null ? username : "unknown user") + ": " + text);

                if (text.startsWith("LOGIN")) {
                    User user = LoginHandler.handleLogin(text, writer);
                    if (user != null) {
                        this.username = user.getUsername();
                        this.userId = user.getUserId();
                        Logger.log("User logged in: " + username);
                    } else {
                        Logger.log("Login failed for user: " + text.split(" ")[1]);
                    }
                } else if (text.equals("LOGOUT")) {
                    writer.println("END_OF_RESPONSE");
                    writer.flush();
                    Logger.log("User logged out: " + username);
                    username = null;
                } else if (username != null) {
                    handleUserCommand(text);
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

    private void handleUserCommand(String text) {
        try {
            if (text.startsWith("ADMIN")) {
                new AdminHandler().handle(text, writer);
            } else if (text.startsWith("CHEF")) {
                new ChefHandler().handle(text, writer);
            } else if (text.startsWith("EMPLOYEE")) {
                new EmployeeHandler().handle(text, writer, reader, userId);
            } else {
                writer.println("Invalid command.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
