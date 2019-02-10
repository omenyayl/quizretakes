package quizretakes.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import quizretakes.Main;
import quizretakes.utils.QuizXMLFile;

import java.io.File;
import java.io.IOException;

public class LoginController {

    public TextField textfield_login_id;
    public Text textError;

    private static final String ERROR_CODE_INVALID_COURSEID = "Error 100: Cannot find ";


    public void onButtonLoginSubmit(ActionEvent actionEvent) {
        String courseID = textfield_login_id.getText();

        File courseDir = new File(
                String.join(
                        System.getProperty("user.dir"),
                        QuizXMLFile.getCourseFilename(courseID))
        );

        if (!courseID.isEmpty() && courseDir.exists()) {
            try {
                Main.pCourseID = courseID;
                Main.switchScene("/layouts/schedule.fxml", getClass(), 800, 600);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            textError.setText(String.format("%s '%s'",
                    ERROR_CODE_INVALID_COURSEID, courseDir.getName()));
            textError.setWrappingWidth(200);
            textError.setVisible(true);
        }
    }
}
