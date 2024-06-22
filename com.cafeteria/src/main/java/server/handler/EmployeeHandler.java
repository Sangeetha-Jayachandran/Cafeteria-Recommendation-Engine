package server.handler;

import util.Logger;

import java.io.PrintWriter;

public class EmployeeHandler {
    public void handle(String command, PrintWriter writer) {
        switch (command) {
            case "EMPLOYEE VIEW_RECOMMENDATION":
                writer.println("Viewing recommendations for the next day...");
                Logger.log("Employee viewed recommendations for the next day.");
                break;
            case "EMPLOYEE VOTE_MENU_ITEM":
                writer.println("Voted for menu item.");
                Logger.log("Employee voted for menu item.");
                break;
            case "EMPLOYEE GIVE_FEEDBACK":
                writer.println("Feedback submitted.");
                Logger.log("Employee submitted feedback.");
                break;
            default:
                writer.println("Invalid employee command.");
                break;
        }
    }
}
