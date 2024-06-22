package client;

import util.Logger;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import client.handler.CommandHandler;
import client.handler.RoleMenuHandler;

public class Client {
	private static final String HOST = "localhost";
	private static final int PORT = 9999;

	public void start() {
		try (Socket socket = new Socket(HOST, PORT);
				OutputStream output = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(output, true);
				InputStream input = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				Scanner scanner = new Scanner(System.in)) {

			System.out.println("Connected to the server.");
			Logger.log("Connected to the server.");
			boolean running = true;
			while (running) {
				String role = null;
				boolean loggedIn = false;
				while (!loggedIn) {
					RoleMenuHandler.showRoleMenu();
					int roleChoice;
					try {
						roleChoice = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("Invalid input. Please enter a number.");
						continue;
					}
					role = RoleMenuHandler.getRole(roleChoice);
					if (role == null) {
						System.out.println("Invalid choice. Please try again.");
						continue;
					}
					System.out.println("Enter your username:");
					String username = scanner.nextLine();
					System.out.println("Enter your password:");
					String password = scanner.nextLine();
					writer.println("LOGIN " + username + " " + password);
					writer.flush();
					String response = reader.readLine();
					System.out.println(response);
					if (response.contains("Login successful")) {
						loggedIn = true;
					} else {
						System.out.println("Login failed. Please try again.");
					}
				}
				while (loggedIn) {
					RoleMenuHandler.showMenu(role);
					int commandChoice;
					try {
						commandChoice = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("Invalid input. Please enter a number.");
						continue;
					}
					String command = CommandHandler.getCommand(role, commandChoice, scanner);
					if (command == null) {
						System.out.println("Invalid choice. Please try again.");
						continue;
					}
					writer.println(command);
					writer.flush();
					StringBuilder fullResponse = new StringBuilder();
					String response;
					while ((response = reader.readLine()) != null && !response.equals("END_OF_RESPONSE")) {
						fullResponse.append(response).append("\n");
					}
					System.out.println(fullResponse.toString());
					if (command.equals("LOGOUT")) {
						System.out.println("Logged out successfully.");
						loggedIn = false;
					}
				}
			}
		} catch (UnknownHostException ex) {
			System.out.println("Server not found: " + ex.getMessage());
			Logger.log("Server not found: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("I/O error: " + ex.getMessage());
			Logger.log("I/O error: " + ex.getMessage());
		}
	}
}