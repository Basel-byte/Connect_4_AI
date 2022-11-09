package com.example.connect_4_ai;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Parent root = loader.load();
        HelloController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root, 552, 580);
        stage.setScene(scene);
        String icon = "file:src/main/resources/images/connect 4 Icon.jpg";
        stage.getIcons().add(new Image(icon));
        stage.setTitle("Connect 4");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}