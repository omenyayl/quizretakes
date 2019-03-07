package quizretakes.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import quizretakes.Layouts;
import quizretakes.Main;
import quizretakes.utils.Common;
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
            Config.getInstance().setCourseID(courseID);
        }
        else {
            Config.getInstance().setCourseID(courseID);
        }

        try {
            Config.getInstance().setCourseID(courseID);
            Main.switchScene(Layouts.SCHEDULE, getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
