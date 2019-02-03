package quizretakes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;
        pStage.setTitle("Quiz Retakes");
        switchScene("/layouts/login.fxml", getClass());
    }

    public static Stage getStage(){
        return pStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchScene(String resourcePath, Class<?> context) throws IOException {
        Parent root = FXMLLoader.load(context.getResource(resourcePath));
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add(context.getResource("/bootstrap3.css").toExternalForm());
        pStage.setScene(scene);
        pStage.show();
    }
}
