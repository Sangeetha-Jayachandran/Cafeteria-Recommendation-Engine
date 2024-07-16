package client.handler;

import java.util.Scanner;

public class ChefCommandHandler {

    public static String getChefCommand(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                return "CHEF VIEW_MENU";
            case 2:
                return "CHEF VIEW_FEEDBACK";
            case 3:
                return "CHEF VIEW_RECOMMENDATION";
            case 4:
                return getRollOutMenuCommand(scanner);
            case 5:
                return "CHEF VIEW_ROLL_OUT_MENU";
            case 6:
                return "LOGOUT";
            default:
                return null;
        }
    }

    private static String getRollOutMenuCommand(Scanner scanner) {
        System.out.println("Enter 5 item IDs for Breakfast (comma-separated):");
        String breakfastItems = scanner.nextLine().trim();
        System.out.println("Enter 5 item IDs for Lunch (comma-separated):");
        String lunchItems = scanner.nextLine().trim();
        System.out.println("Enter 5 item IDs for Dinner (comma-separated):");
        String dinnerItems = scanner.nextLine().trim();
        return String.format("CHEF ROLL_OUT_MENU,%s,%s,%s", breakfastItems, lunchItems, dinnerItems);
    }
}
