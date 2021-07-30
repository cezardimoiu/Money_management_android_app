package com.example.money_management_app;



import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private static User single_instance = null;

    private String name;
    private String email;
    public HashMap<String, ArrayList<Double>> categories;

    private User() {
        categories =  new HashMap<String, ArrayList<Double>>();

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

    public void addCategoryRon(@NotNull String category) {
        this.categories.put(category, new ArrayList<>());
    }

    public void modifyCategory(@NotNull String category, ArrayList<Double> values) {
        this.categories.put(category, values);
    }

    public void deleteCategory(@NotNull String category) {
        this.categories.remove(category);
    }

    public ArrayList<Double> getCategory(@NotNull String category) {
        return this.categories.get(category);
    }

    public Double getEuroFromCategory(@NotNull String category) {
        return this.categories.get(category).get(1);
    }

    public Double getRonFromCategory(@NotNull String category) {
        return this.categories.get(category).get(0);
    }
}
