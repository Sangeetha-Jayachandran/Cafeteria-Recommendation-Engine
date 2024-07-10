package client.handler;

import java.util.Scanner;

public class EmployeeCommandHandler {
	static String getEmployeeCommand(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                return "EMPLOYEE VIEW_RECOMMENDATION";
            case 2:
                return "EMPLOYEE VOTE_MENU_ITEM";
            case 3:
                return getEmployeeFeedbackCommand(scanner);
            case 4:
                return "LOGOUT";
            default:
                return null;
        }
    }

	   
    private static String getEmployeeFeedbackCommand(Scanner scanner) {
        System.out.println("Enter the item ID you want to give feedback on:");
        int itemId = Integer.parseInt(scanner.nextLine());
        System.out.println("Enter your comment:");
        String comment = scanner.nextLine();
        System.out.println("Enter your rating (1-5):");
        int rating = Integer.parseInt(scanner.nextLine());
        return String.format("EMPLOYEE GIVE_FEEDBACK,%d,%s,%d", itemId, comment, rating);
    }
}
