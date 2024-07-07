package org.example.lscinvoicebuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Storage storage = new Storage();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("mainScreen.fxml"));
        FXMLLoader fxmlLoader2 = new FXMLLoader(MainApplication.class.getResource("secondScreen.fxml"));

        MainController mainController = new MainController();
        SecondController secondController = new SecondController();

        fxmlLoader.setController(mainController);
        fxmlLoader2.setController(secondController);

        try {
            Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
            Scene scene2 = new Scene(fxmlLoader2.load(), 1000, 1000);

            stage.setTitle("LSC Invoice Builder");
            stage.setScene(scene);
            stage.show();

            mainController.setStorage(storage);
            mainController.refreshDropdown();
            mainController.setStage(stage);
            mainController.setSecondController(secondController);
            mainController.setScene2(scene2);


            secondController.setStage(stage);
            secondController.setMainController(mainController);
            secondController.setScene1(scene);
            secondController.setStorage(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}