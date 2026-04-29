package com.expensetracker.services;

import com.expensetracker.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.TestInstance;


import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//Annotation to allow not making BeforeEach implementation static
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChartServiceTest {

    //Data for tests
    ObservableList<Expense> expenses;
    ChartService chartService;
    FilteredList<Expense> chartInput;
    Map<String, Double> dataForPieTest;
    Map<Integer, Double> dataForLineTest;
    Map<String, HashMap<Integer, Double>> dataForBarTest;

    @BeforeAll
    static void initJFX() {
        new JFXPanel();

    }

    @BeforeEach
    public void prepareData(){
        chartService = new ChartService();
        dataForPieTest = new HashMap<>();
        dataForLineTest = new HashMap<>();
        dataForBarTest = new HashMap<>();

        Expense ex1 = new Expense("Food", 3, LocalDate.of(2022, 2, 1));
        Expense ex2 = new Expense("Technologies", 1000, LocalDate.of(2022, 2, 2));
        Expense ex3 = new Expense("Entertainment", 120, LocalDate.of(2022, 2, 3));
        Expense ex4 = new Expense("Entertainment", 200, LocalDate.of(2022, 2, 3));

        expenses = FXCollections.observableArrayList();
        expenses.add(ex1);
        expenses.add(ex2);
        expenses.add(ex3);
        expenses.add(ex4);
        chartInput = new FilteredList<>(expenses);
        expenses.forEach(expense -> {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            int month = expense.getDate().getMonthValue();

            dataForPieTest.merge(category, amount, Double::sum);
            dataForLineTest.merge(month, amount, Double::sum);
            dataForBarTest.computeIfAbsent(category, k-> new HashMap<>()).merge(month, amount, Double::sum);

        });


        }

    @Test
    public void createPieChartTest(){
        Map<String, Double> graphData= new HashMap<>();
        PieChart pc = chartService.createPieChart(chartInput);
        //Reading chart data
        pc.getData().forEach(expense ->{
            graphData.merge(expense.getName(), expense.getPieValue(), Double::sum);
        });

        assertEquals(dataForPieTest, graphData);

    }

    @Test
    public void createLineChartTest(){
        Map<Integer, Double> graphData = new HashMap<>();
        LineChart<Number, Number> lc = chartService.createLineChart(chartInput, 2022);
        //Reading chart data
        for (var series : lc.getData()) {
            for (var dataPoint : series.getData()) {
                graphData.merge(dataPoint.getXValue().intValue(), dataPoint.getYValue().doubleValue(), Double::sum);
            }
        }
        assertEquals(dataForLineTest, graphData);

    }

    @Test
    public void createBarChart(){
        Map<String, HashMap<Integer, Double>> chartData = new HashMap<>();
        BarChart<String, Number> barChart = chartService.createBarChart(chartInput);
        //reading chart data
        for(var series: barChart.getData()){
            String month = series.getName();
            HashMap<Integer, Double> monthData = new HashMap<>();
            for(var dataPoint : series.getData()){
                monthData.put(Integer.parseInt(dataPoint.getXValue()), dataPoint.getYValue().doubleValue());
            }
            chartData.put(month, monthData);
        }
        assertEquals(dataForBarTest, chartData);
    }
}