package com.expensetracker.services;

import com.expensestorage.ExpenseStorage;
import com.expensetracker.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//Service responsible for the operations with expenses
public class ExpenseService {
    //Observable list holding the expenses
    private final ObservableList<Expense> expenses = FXCollections.observableArrayList();

    //Loading the data from json
    public ExpenseService(){
        expenses.addAll(ExpenseStorage.loadExpenses());
    }

    //Function for getting the expenses from the list
    public ObservableList<Expense> getExpenses(){
        return expenses;
    }

    //Adding the expense to the list and persisting changes to json
    public void addExpense(String category, double amount, LocalDate date){
        Expense expense = new Expense(category, amount, date);
        expenses.add(expense);
        ExpenseStorage.saveExpenses(expenses);
    }

    //Function for removing selected expenses from the list and persisting changes to json
    public void deleteExpenses(List<Expense> expensesToDelete) {
        Set<UUID> idsToDelete = expensesToDelete.stream()
                .map(Expense::getId)
                .collect(Collectors.toSet());

        expenses.removeIf(expense -> idsToDelete.contains(expense.getId()));

        ExpenseStorage.saveExpenses(expenses);
    }

    //Function which returns the total sum of all the expenses in the table
    public double getTotalSum(){
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    //Function returning a set of unique categories from the expenses
    public Set<String> getCategories(){
        return expenses.stream().map(Expense::getCategory).collect(Collectors.toSet());
    }

    public Expense getExpensebyId(UUID id){
        for(Expense expense: expenses){
            if(expense.getId().equals(id)) return expense;
        };
        return null;
    }

}
