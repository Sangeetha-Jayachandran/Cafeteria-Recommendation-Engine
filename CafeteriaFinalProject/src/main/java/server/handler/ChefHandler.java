package server.handler;

import util.Logger;

import java.io.PrintWriter;

public class ChefHandler {
    public void handle(String command, PrintWriter writer) {
        switch (command) {
            case "CHEF VIEW_MENU":
                writer.println("Viewing menu...");
                Logger.log("Chef viewed the menu.");
                break;
            case "CHEF VIEW_FEEDBACK":
                writer.println("Viewing feedback...");
                Logger.log("Chef viewed feedback.");
                break;
            case "CHEF VIEW_RECOMMENDATION":
                writer.println("Viewing recommendations...");
                Logger.log("Chef viewed recommendations.");
                break;
            case "CHEF ROLL_OUT_MENU":
                writer.println("Menu rolled out for next day.");
                Logger.log("Chef rolled out menu for the next day.");
                break;
            case "CHEF VIEW_ROLL_OUT_MENU":
                writer.println("Viewing rolled out menu...");
                Logger.log("Chef viewed rolled out menu.");
                break;
            case "CHEF GENERATE_REPORT":
                writer.println("Generated monthly feedback report.");
                Logger.log("Chef generated monthly feedback report.");
                break;
            default:
                writer.println("Invalid chef command.");
                break;
        }
    }
}
