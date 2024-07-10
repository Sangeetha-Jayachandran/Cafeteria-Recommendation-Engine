package client.handler;

import java.util.Scanner;

public class AdminCommandHandler {
    public static String getAdminCommand(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                return getAddUserCommand(scanner);
            case 2:
                return "ADMIN VIEW_USERS";
            case 3:
                return "ADMIN VIEW_MENU";
            case 4:
                return getAddMenuItemCommand(scanner);
            case 5:
                return getUpdateMenuItemCommand(scanner);
            case 6:
                return deleteMenuItemCommand(scanner);
            case 7:
                return "LOGOUT";
            default:
                return null;
        }
    }

    private static String getAddUserCommand(Scanner scanner) {
        System.out.println("Enter the username:");
        String newUsername = scanner.nextLine();
        System.out.println("Enter the password:");
        String newPassword = scanner.nextLine();
        System.out.println("Enter the user's role (1 for Admin, 2 for Chef, 3 for Employee):");
        int roleId = Integer.parseInt(scanner.nextLine());
        return String.format("ADMIN REGISTER_USER,%s,%s,%d", newUsername, newPassword, roleId);
    }

    private static String getAddMenuItemCommand(Scanner scanner) {
        System.out.println("Enter item name:");
        String itemName = scanner.nextLine();
        System.out.println("Enter item type (1 for Breakfast, 2 for Lunch, 3 for Dinner):");
        int itemTypeChoice = Integer.parseInt(scanner.nextLine());
        String itemType = getItemType(itemTypeChoice);
        System.out.println("Enter item price:");
        double itemPrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Is the item available (yes/no):");
        String availabilityInput = scanner.nextLine().toLowerCase();
        boolean itemAvailability = availabilityInput.equals("yes");
        return String.format("ADMIN ADD_MENU_ITEM,%s,%s,%.2f,%b", itemName, itemType, itemPrice, itemAvailability);
    }

    private static String getUpdateMenuItemCommand(Scanner scanner) {
        System.out.println("Enter item id:");
        int itemId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter item name:");
        String updateName = scanner.nextLine();
        System.out.println("Enter item type (1 for Breakfast, 2 for Lunch, 3 for Dinner):");
        int updateTypeChoice = Integer.parseInt(scanner.nextLine());
        String updateType = getItemType(updateTypeChoice);
        System.out.println("Enter item price:");
        double updatePrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Is the item available (yes/no):");
        String updateAvailabilityInput = scanner.nextLine().toLowerCase();
        boolean updateAvailability = updateAvailabilityInput.equals("yes");
        return String.format("ADMIN UPDATE_MENU_ITEM,%d,%s,%s,%.2f,%b", itemId, updateName, updateType, updatePrice, updateAvailability);
    }

    private static String deleteMenuItemCommand(Scanner scanner) {
        System.out.println("Enter item id to delete:");
        int itemIdToDelete = Integer.parseInt(scanner.nextLine());
        return "ADMIN DELETE_MENU_ITEM," + itemIdToDelete;
    }

    private static String getItemType(int itemTypeChoice) {
        switch (itemTypeChoice) {
            case 1:
                return "Breakfast";
            case 2:
                return "Lunch";
            case 3:
                return "Dinner";
            default:
                System.out.println("Invalid choice. Defaulting to Breakfast.");
                return "Breakfast";
        }
    }
}
