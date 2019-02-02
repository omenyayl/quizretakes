package quizretakes.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class LoginController {


    public TextField textfield_login_id;

    public void onButtonLoginSubmit(ActionEvent actionEvent) {
        System.out.println("Submitted: " + textfield_login_id.getText());
    }
}
