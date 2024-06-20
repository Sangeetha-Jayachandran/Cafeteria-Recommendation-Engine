package com.cafeteria.server.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
    private static final String LOG_FILE = "user_activity.log";

    public static synchronized void log(String username, String message) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(new Date() + " - " + username + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
