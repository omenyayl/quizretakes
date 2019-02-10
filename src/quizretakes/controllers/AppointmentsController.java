package quizretakes.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import quizretakes.Layouts;
import quizretakes.Main;
import quizretakes.utils.ApptBean;
import quizretakes.utils.ApptsReader;

import java.io.IOException;
import java.util.ArrayList;

public class AppointmentsController {

    public ScrollPane scrollPaneAppointments;
    public ListView listViewAppointments;
    public Text textError;

    private void initViews() {
        scrollPaneAppointments.widthProperty().addListener((obs, oldVal, newVal) -> {
            listViewAppointments.setPrefWidth(newVal.doubleValue());
        });

    }

    @FXML
    public void initialize() {
        initViews();
        readAppointmentsData();
    }

    private void readAppointmentsData() {
        try {
            ArrayList appts = new ApptsReader().read("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickButtonBack(ActionEvent actionEvent) throws IOException {
        Main.switchScene(Layouts.SCHEDULE.toString(), getClass(), 800, 600);
    }

    private void updateErrors(String...errors) {
        if (errors.length != 0) {
            textError.setVisible(true);
            textError.setText(String.join("\n", errors));
        }
        else {
            textError.setVisible(false);
        }
    }
}
