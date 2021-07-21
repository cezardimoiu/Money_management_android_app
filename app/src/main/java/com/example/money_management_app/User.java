package com.example.money_management_app;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static User single_instance = null;

    private String name;
    private String email;
    public HashMap<String, ArrayList<Double>> categories;
    private String categorie;

    private User() {
        categories =  new HashMap<String, ArrayList<Double>>();
        ArrayList<Double> test = new ArrayList<>();
        test.add(2.23);
        categories.put("abcd", test);
    }

    public void resetUser() {
        this.email = null;
    }

    public static User getInstance() {
        if (single_instance == null) {
            single_instance = new User();
        }

        return single_instance;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
