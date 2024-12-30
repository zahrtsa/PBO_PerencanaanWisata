package com.desty5.models;

public class SessionManager {

    private static int currentUserId = -1;

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }
}
