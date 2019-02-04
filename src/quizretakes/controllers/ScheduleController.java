package quizretakes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;
import quizretakes.Main;
import quizretakes.utils.QuizXMLFile;
import quizretakes.utils.courseBean;
import quizretakes.utils.courseReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class ScheduleController {
    public TextField textFieldName;
    public Text textDates;
    public Text textCourseID;
    public ListView<String> listViewAllOpportunities;
    public ScrollPane scrollPaneAllOpportunities;
    public ScrollPane scrollPaneRequestRetakes;
    public ListView listViewRequestRetakes;
    public GridPane gridPaneRetakes;
    public Text textError;

    private String mCourseID;

    private String courseFileName;

    private static final String ERROR_NO_DATA_FILE = "Error 101: Cannot find data files";

    private void initViews() {
        scrollPaneAllOpportunities.widthProperty().addListener((obs, oldVal, newVal) -> {
            listViewRequestRetakes.setPrefWidth(newVal.doubleValue());
            listViewAllOpportunities.setPrefWidth(newVal.doubleValue());
        });

        scrollPaneAllOpportunities.heightProperty().addListener((obs, oldVal, newVal) -> {
            listViewRequestRetakes.setPrefHeight(newVal.doubleValue());
            listViewRequestRetakes.setPrefHeight(newVal.doubleValue());
        });

        Main.getStage().heightProperty().addListener((obs, oldVal, newVal) -> {
            gridPaneRetakes.setPrefHeight(newVal.doubleValue() * 0.7);
        });

        textError.setWrappingWidth(Main.getStage().getWidth()*0.8);

    }

    void readDataFiles(String courseID) {
        courseBean course;
        courseReader cr = new courseReader();

        courseFileName = QuizXMLFile.getCourseFilename(courseID);

        try {
            course = cr.read(courseFileName);
            return;
        } catch (IOException | ParserConfigurationException | SAXException e) {
            String message = String.format("%s for %s", ERROR_NO_DATA_FILE, courseID);
            textError.setText(message);
            textError.setVisible(true);
        }

        String quizzesFilename = QuizXMLFile.getQuizzesFilename(courseID);
        String retakesFilename = QuizXMLFile.getRetakesFilename(courseID);
        String apptsFilename = QuizXMLFile.getApptsFilename(courseID);



    }

    void initData(String courseID)  {
        this.mCourseID = courseID;

        initViews();

        readDataFiles(courseID);
    }

    public void onClickButtonBack(ActionEvent actionEvent) {
        try {
            Main.switchScene("/layouts/login.fxml", getClass(), 300, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickButtonSubmitRequest(ActionEvent actionEvent) {
    }

    public void onClickButtonRefresh(ActionEvent actionEvent) {
        readDataFiles(mCourseID);
    }
}
