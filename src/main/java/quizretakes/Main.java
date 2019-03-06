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
        switchScene(Layouts.LOGIN, getClass(), 300, 300);
    }

    public static Stage getStage(){
        return pStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchScene(Layouts resourcePath, Class<?> context, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(context.getResource(resourcePath.toString()));
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(context.getResource("/bootstrap3.css").toExternalForm());

        pStage.setScene(scene);
        pStage.show();
    }

}

/*
TODO:

User story:
As an instructor, I want to define scheduled quizzes and retakes using the UI so that I don't have to
hand-edit the XML files by hand.

Acceptance Test:

- Simulate the user adding a quiz and verify that it is added in the lists of quizzes in the XML.
- Simulate the user adding a retake and verify that it is added in the lists of retakes in the XML.
- Simulate the user deleting a retake and verify that the lists of retakes in the XML no longer has that retake.
- Simulate the user deleting a quiz and verify that the lists of quizzes in the XML no longer has that retake.
- Verify that the program displays an error when the user attempts to add a quiz or a retake with empty information.
- Verify that an error is shown when the user attempts to create a quiz with no id or no date.
- Verify that an error is shown when the user attempts to create a retake with no id or no location or no date.

 */