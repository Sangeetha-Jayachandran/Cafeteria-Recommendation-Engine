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

public class ServerThread extends Thread {
	private Socket socket;
	private String username;

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
					username = LoginHandler.handleLogin(text, writer);
					if (username != null) {
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
		if (text.startsWith("ADMIN")) {
			new AdminHandler().handle(text, writer);
		} else if (text.startsWith("CHEF")) {
			new ChefHandler().handle(text, writer);
		} else if (text.startsWith("EMPLOYEE")) {
			new EmployeeHandler().handle(text, writer);
		} else {
			writer.println("Invalid command.");
		}
	}
}
