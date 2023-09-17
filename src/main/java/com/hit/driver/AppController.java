package com.hit.driver;

import com.hit.server.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppController extends Application {

    private Server server;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppController.class.getResource("EntranceForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Restaurant Management System");
        stage.setMinHeight(439);
        stage.setMinWidth(600);
        stage.setScene(scene);
        stage.show();

        int port = 1234;
        server = new Server(port);
        new Thread(server::run).start();
    }

    @Override
    public void stop() {
        if (server != null) {
            server.stop();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}