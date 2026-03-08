package com.expensetracker;

import com.expensetracker.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import com.expensestorage.ExpenseStorage;

public class MainController {
    @FXML
    private TableView<Expense> expenseTable;
    @FXML
    private TableColumn<Expense, String > categoryColumn;
    @FXML
    private TableColumn<Expense, Double> amountColumn;
    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;

    private ObservableList<Expense> expenses = FXCollections.observableArrayList();

    @FXML
    private TextField categoryField;

    @FXML
    private TextField amountField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label totalSum;


    @FXML
    public void initialize(){
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        expenses.addAll(ExpenseStorage.loadExpenses());
        expenseTable.setItems(expenses);
        expenseTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        datePicker.setDayCellFactory(picker-> new DateCell(){
            @Override
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);

                if(date.isAfter(LocalDate.now())){
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
    }

    @FXML
    private void updateTotalSum(){
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        totalSum.setText("Total: " + total);
    }
    @FXML
    private void addExpense(){
        String categoryToAdd = categoryField.getText();
        double amountToAdd = Double.parseDouble(amountField.getText());
        LocalDate date = datePicker.getValue();

        Expense expense = new Expense(categoryToAdd, amountToAdd, date);
        expenses.add(expense);
        updateTotalSum();
        ExpenseStorage.saveExpenses(expenses);
        categoryField.clear();
        amountField.clear();
        datePicker.setValue(null);
    }


    @FXML
    private void deleteExpense(){
        var selected = expenseTable.getSelectionModel().getSelectedItems();

        if(!selected.isEmpty()){
            expenses.removeAll(selected);
            ExpenseStorage.saveExpenses(expenses);
        }

    }

}
