package quizretakes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import quizretakes.Main;
import quizretakes.SceneFactory;

import java.io.IOException;

public class LoginController {

    public TextField textfield_login_id;

    public void onButtonLoginSubmit(ActionEvent actionEvent) {
        String courseID = textfield_login_id.getText();

        if (!courseID.isEmpty()) {
            try {
                Main.switchScene("/layouts/schedule.fxml", getClass());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
