package quizretakes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/login.fxml"));
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
