package com.cafeteria.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        // Start the database cleanup task
        ServerThread.startCleanupTask();

        // Your existing server startup code...
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port 8080");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
