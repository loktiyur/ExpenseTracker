package com.expensetracker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/expensetracker/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),600,400);
        scene.getStylesheets().add(
                getClass().getResource("/com/expensetracker/styles.css").toExternalForm()
        );

        stage.setTitle("Expense tracker");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){
        launch();
    }
}
