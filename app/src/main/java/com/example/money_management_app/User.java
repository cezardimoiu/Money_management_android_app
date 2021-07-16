package com.example.money_management_app;

public class User {
    private static User single_instance = null;

    private User() {

    }

    public static User getInstance() {
        if (single_instance == null) {
            single_instance = new User();
        }

        return single_instance;
    }
}
