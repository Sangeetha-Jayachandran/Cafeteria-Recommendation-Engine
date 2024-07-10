package client.handler;

import java.util.Scanner;

public class CommandHandler {
    public static String getCommand(String role, int choice, Scanner scanner) {
        switch (role) {
            case "ADMIN":
                return AdminCommandHandler.getAdminCommand(choice, scanner);
            case "CHEF":
                return ChefCommandHandler.getChefCommand(choice);
            case "EMPLOYEE":
                return EmployeeCommandHandler.getEmployeeCommand(choice, scanner);
            default:
                return null;
        }
    }
}
