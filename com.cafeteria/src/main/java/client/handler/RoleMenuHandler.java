package client.handler;

public class RoleMenuHandler {
    public static void showRoleMenu() {
        System.out.println("\nChoose your role: ");
        System.out.println("1. ADMIN");
        System.out.println("2. CHEF");
        System.out.println("3. EMPLOYEE");
        System.out.println("4. EXIT");
    }

    public static String getRole(int roleChoice) {
        switch (roleChoice) {
            case 1:
                return "ADMIN";
            case 2:
                return "CHEF";
            case 3:
                return "EMPLOYEE";
            case 4:
                System.exit(0);
                return null;
            default:
                return null;
        }
    }

    public static void showMenu(String role) {
        switch (role) {
            case "ADMIN":
                showAdminMenu();
                break;
            case "CHEF":
                showChefMenu();
                break;
            case "EMPLOYEE":
                showEmployeeMenu();
                break;
        }
    }

    private static void showAdminMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. Register User");
        System.out.println("2. View Users");
        System.out.println("3. View Menu");
        System.out.println("4. Add Menu Item");
        System.out.println("5. Update Menu Item");
        System.out.println("6. Delete Menu Item");
        System.out.println("7. Logout");
    }

    private static void showChefMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. View Menu");
        System.out.println("2. View Feedback");
        System.out.println("3. View Recommendation");
        System.out.println("4. Roll Out Menu for Next Day");
        System.out.println("5. View Rollout Menu");
        System.out.println("6. Logout");
    }

    private static void showEmployeeMenu() {
        System.out.println("\nChoose an option:");
        System.out.println("1. View Recommendation for the Next Day");
        System.out.println("2. View Menu");
        System.out.println("3. Give Feedback");
        System.out.println("4. Logout");
    }
}
