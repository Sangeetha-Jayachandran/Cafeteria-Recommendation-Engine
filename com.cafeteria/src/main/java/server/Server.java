package server;

import util.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 9999;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            Logger.log("Server started on port " + PORT);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected");
                    Logger.log("New client connected");

                    new ServerThread(socket).start();
                } catch (IOException e) {
                    System.out.println("Error accepting connection: " + e.getMessage());
                    Logger.log("Error accepting connection: " + e.getMessage());
                }
            }
        } catch (IOException ex) {
            if (ex.getMessage().contains("Address already in use")) {
                System.out.println("Port " + PORT + " is already in use. Please free up the port and try again.");
                Logger.log("Port " + PORT + " is already in use. Please free up the port and try again.");
            } else {
                System.out.println("Server exception: " + ex.getMessage());
                Logger.log("Server exception: " + ex.getMessage());
            }
        }
    }
}
