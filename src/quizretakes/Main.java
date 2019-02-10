package quizretakes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage pStage;
    public static String pCourseID;

    @Override
    public void start(Stage primaryStage) throws Exception{
        pStage = primaryStage;
        pStage.setTitle("Quiz Retakes");
        switchScene(Layouts.LOGIN.toString(), getClass(), 300, 300);
    }

    public static Stage getStage(){
        return pStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchScene(String resourcePath, Class<?> context, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(context.getResource(resourcePath));
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(context.getResource("/bootstrap3.css").toExternalForm());

        pStage.setScene(scene);
        pStage.show();
    }

}
