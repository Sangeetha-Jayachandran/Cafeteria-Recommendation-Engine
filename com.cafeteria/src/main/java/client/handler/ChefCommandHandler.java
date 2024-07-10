package client.handler;

public class ChefCommandHandler {
	static String getChefCommand(int choice) {
        switch (choice) {
            case 1:
                return "CHEF VIEW_MENU";
            case 2:
                return "CHEF VIEW_FEEDBACK";
            case 3:
                return "CHEF VIEW_RECOMMENDATION";
            case 4:
                return "CHEF ROLL_OUT_MENU";
            case 5:
                return "CHEF VIEW_ROLL_OUT_MENU";
            case 6:
                return "LOGOUT";
            default:
                return null;
        }
    }

}
