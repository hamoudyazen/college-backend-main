package com.example.demo;

import com.google.cloud.firestore.annotation.DocumentId;
import java.util.Date;

public class ExpensesAndIncome {
    private String type;
    private String id;
    private double amount;
    private String category;
    private String description;
    private Date date;
    private String userId;

    public ExpensesAndIncome() {
        // Default constructor required for Firestore deserialization
    }

    public ExpensesAndIncome(String type, String id, double amount, String category, String description, Date date, String userId) {
        this.type = type;
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
