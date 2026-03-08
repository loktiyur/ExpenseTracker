package com.expensestorage;
import com.expensetracker.model.Expense;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseStorage {
    private static final String FILENAME = "expenses.json";

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .setPrettyPrinting()
            .create();

    public static void saveExpenses(List<Expense> expenses){
        try(FileWriter writer = new FileWriter(FILENAME)){
            gson.toJson(expenses, writer);
        }catch (Exception e){
            e.printStackTrace();
        }
    };
    public static List<Expense> loadExpenses(){
        try(FileReader reader = new FileReader(FILENAME)){
            Type listType = new TypeToken<ArrayList<Expense>>(){}.getType();
            List<Expense> expenses = gson.fromJson(reader, listType);
            if(expenses == null){
                return new ArrayList<>();
            }
            return expenses;
        }catch(Exception e){
            return new ArrayList<>();
        }
    }

}
