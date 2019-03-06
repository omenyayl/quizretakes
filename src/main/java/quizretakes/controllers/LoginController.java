package quizretakes.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import quizretakes.Layouts;
import quizretakes.Main;
import quizretakes.utils.Config;

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
                        Config.getCourseFilename(courseID))
        );

        if (!courseID.isEmpty() && courseDir.exists()) {
            try {
                Config.getInstance().setCourseID(courseID);
                Main.switchScene(Layouts.SCHEDULE, getClass(), 800, 600);
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
