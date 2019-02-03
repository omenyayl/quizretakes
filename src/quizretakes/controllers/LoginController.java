package quizretakes.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import quizretakes.Main;
import quizretakes.utils.QuizXMLFile;

import java.io.File;
import java.io.IOException;

public class LoginController {

    public TextField textfield_login_id;


    public void onButtonLoginSubmit(ActionEvent actionEvent) {
        String courseID = textfield_login_id.getText();

        File tmpDir = new File(
                String.join(
                        System.getProperty("user.dir"),
                        QuizXMLFile.getCourseFilename(courseID))
        );

        System.out.println(tmpDir.getPath());

        System.out.println(tmpDir.exists());
        if (!courseID.isEmpty()) {
            try {
                Main.switchScene("/layouts/schedule.fxml", getClass(), 800, 600);
                System.out.println(System.getProperty("user.dir"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
