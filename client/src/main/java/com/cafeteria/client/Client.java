package com.cafeteria.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println((String) in.readObject());
                int roleChoice = scanner.nextInt();
                scanner.nextLine();
                out.writeObject(roleChoice);

                switch (roleChoice) {
                    case 1:
                        handleAdmin(in, out, scanner);
                        break;
                    case 2:
                        handleChef(in, out, scanner);
                        break;
                    case 3:
                        handleEmployee(in, out, scanner);
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Goodbye!");
                        return;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void handleAdmin(ObjectInputStream in, ObjectOutputStream out, Scanner scanner) throws IOException, ClassNotFoundException {
        while (true) {
            System.out.println((String) in.readObject());
            int adminChoice = scanner.nextInt();
            scanner.nextLine(); 
            out.writeObject(adminChoice);

            switch (adminChoice) {
                case 1:
                    System.out.println((String) in.readObject());
                    String username = scanner.nextLine();
                    out.writeObject(username);
                    System.out.println((String) in.readObject());
                    String password = scanner.nextLine();
                    out.writeObject(password);

                    String response = (String) in.readObject();
                    System.out.println(response);
                    if ("Invalid admin credentials.".equals(response)) {
                        break;
                    } else {
                        handleAdminActions(in, out, scanner);
                    }
                    break;
                case 2:
                    return;
            }
        }
    }

    private static void handleAdminActions(ObjectInputStream in, ObjectOutputStream out, Scanner scanner) throws IOException, ClassNotFoundException {
        while (true) {
            System.out.println((String) in.readObject());
            int actionChoice = scanner.nextInt();
            scanner.nextLine();
            out.writeObject(actionChoice);

            switch (actionChoice) {
                case 1:
                    System.out.println((String) in.readObject());
                    String newUsername = scanner.nextLine();
                    out.writeObject(newUsername);
                    System.out.println((String) in.readObject());
                    String newPassword = scanner.nextLine();
                    out.writeObject(newPassword);
                    System.out.println((String) in.readObject());
                    String newRole = scanner.nextLine();
                    out.writeObject(newRole);
                    System.out.println((String) in.readObject());
                    break;
                case 2:
                    System.out.println((String) in.readObject());
                    String itemName = scanner.nextLine();
                    out.writeObject(itemName);
                    System.out.println((String) in.readObject());
                    String category = scanner.nextLine();
                    out.writeObject(category);
                    System.out.println((String) in.readObject());
                    double price = scanner.nextDouble();
                    out.writeObject(price);
                    System.out.println((String) in.readObject());
                    boolean availability = scanner.nextBoolean();
                    out.writeObject(availability);
                    scanner.nextLine();
                    System.out.println((String) in.readObject());
                    break;
                case 3:
                    System.out.println((String) in.readObject());
                    int updateItemId = scanner.nextInt();
                    out.writeObject(updateItemId);
                    scanner.nextLine();
                    System.out.println((String) in.readObject());
                    String updateItemName = scanner.nextLine();
                    out.writeObject(updateItemName);
                    System.out.println((String) in.readObject());
                    String updateCategory = scanner.nextLine();
                    out.writeObject(updateCategory);
                    System.out.println((String) in.readObject());
                    double updatePrice = scanner.nextDouble();
                    out.writeObject(updatePrice);
                    System.out.println((String) in.readObject());
                    boolean updateAvailability = scanner.nextBoolean();
                    out.writeObject(updateAvailability);
                    scanner.nextLine();
                    System.out.println((String) in.readObject());
                    break;
                case 4:
                    System.out.println((String) in.readObject());
                    int deleteItemId = scanner.nextInt();
                    out.writeObject(deleteItemId);
                    scanner.nextLine();
                    System.out.println((String) in.readObject());
                    break;
                case 5:
                    System.out.println((String) in.readObject());
                    break;
                case 6:
                    return;
            }
        }
    }

    private static void handleChef(ObjectInputStream in, ObjectOutputStream out, Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.println((String) in.readObject());
        String username = scanner.nextLine();
        out.writeObject(username);
        System.out.println((String) in.readObject());
        String password = scanner.nextLine();
        out.writeObject(password);

        String response = (String) in.readObject();
        System.out.println(response);
        if ("Invalid chef credentials.".equals(response)) {
            return;
        }

        while (true) {
            System.out.println((String) in.readObject());
            int chefChoice = scanner.nextInt();
            scanner.nextLine();
            out.writeObject(chefChoice);

            switch (chefChoice) {
                case 1:
                    System.out.println((String) in.readObject());

                    System.out.println((String) in.readObject());
                    System.out.println("Enter 2 item IDs for breakfast (space-separated):");
                    List<Integer> breakfastItems = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        breakfastItems.add(scanner.nextInt());
                    }
                    scanner.nextLine();
                    out.writeObject(breakfastItems);

                    System.out.println((String) in.readObject());
                    System.out.println("Enter 2 item IDs for lunch (space-separated):");
                    List<Integer> lunchItems = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        lunchItems.add(scanner.nextInt());
                    }
                    scanner.nextLine();
                    out.writeObject(lunchItems);

                    System.out.println((String) in.readObject());
                    System.out.println("Enter 2 item IDs for dinner (space-separated):");
                    List<Integer> dinnerItems = new ArrayList<>();
                    for (int i = 0; i < 2; i++) {
                        dinnerItems.add(scanner.nextInt());
                    }
                    scanner.nextLine();
                    out.writeObject(dinnerItems);

                    System.out.println((String) in.readObject());
                    break;
                case 2:
                    System.out.println((String) in.readObject());
                    break;
                case 3:
                    System.out.println((String) in.readObject());
                    break;
                case 4:
                    System.out.println((String) in.readObject());
                    break;
                case 5:
                    System.out.println((String) in.readObject());
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void handleEmployee(ObjectInputStream in, ObjectOutputStream out, Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.println((String) in.readObject());
        String username = scanner.nextLine();
        out.writeObject(username);
        System.out.println((String) in.readObject());
        String password = scanner.nextLine();
        out.writeObject(password);

        String response = (String) in.readObject();
        System.out.println(response);
        if ("Invalid employee credentials.".equals(response)) {
            return;
        }

        // Display notifications and employee menu
        System.out.println((String) in.readObject());
        
        while (true) {
            System.out.println((String) in.readObject());
            int employeeChoice = scanner.nextInt();
            scanner.nextLine();
            out.writeObject(employeeChoice);

            switch (employeeChoice) {
                case 1:
                    System.out.println((String) in.readObject());   //Notifications
                    break;
                case 2:
                    System.out.println("Enter item ID to order or 0 to exit:");
                    int orderId = scanner.nextInt();
                    out.writeObject(orderId);
                    System.out.println((String) in.readObject());
                    break;
                case 3:
                    System.out.println("Enter item ID to give feedback or 0 to exit:");
                    int itemId = scanner.nextInt();
                    scanner.nextLine();
                    out.writeObject(itemId);
                    if (itemId != 0) {
                        System.out.println((String) in.readObject());
                        int rating = scanner.nextInt();
                        out.writeObject(rating);
                        scanner.nextLine();
                        System.out.println((String) in.readObject());
                        String comment = scanner.nextLine();
                        out.writeObject(comment);
                        System.out.println((String) in.readObject());
                    }
                    break;
                case 4:
                    System.out.println((String) in.readObject());
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
