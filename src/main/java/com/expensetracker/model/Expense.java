package com.expensetracker.model;

import java.time.LocalDate;
import java.util.UUID;

public class Expense {
    private UUID id;
    private String category;
    private double amount;
    private LocalDate date;

    public Expense(String category, double amount, LocalDate date){
        this.id = UUID.randomUUID();
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
    public String getCategory(){
        return category;
    }
    public double getAmount(){
        return amount;
    }
    public LocalDate getDate(){
        return date;
    }
    public UUID getId() { return id; }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        else if (!(o instanceof Expense)){
            return false;
        }

        Expense expense = (Expense) o;
        return Double.compare(amount, expense.amount) == 0 &&
                getCategory().equals(expense.getCategory()) &&
                getDate().equals(expense.getDate());
    }

    @Override
    public int hashCode(){
        return java.util.Objects.hash(getCategory(), getAmount(), getDate());
    }
}
