package quizretakes.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;
import quizretakes.Layouts;
import quizretakes.Main;
import quizretakes.utils.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class ScheduleEditorController {

    private static final String ERROR_QUIZ_MISSING_DATA = "New quiz has missing information.";
    private static final String ERROR_RETAKE_MISSING_DATA = "New retake has missing information.";
    private static final String ERROR_INVALID_TIME_RETAKE = "Retake time is invalid.";
    private static final String ERROR_INVALID_RETAKE_ID = "Invalid new retake ID";
    private static final String ERROR_DUPLICATE_RETAKE = "Attempting to add a duplicate retake.";
    private static final String ERROR_INVALID_TIME_QUIZ = "Quiz time is invalid.";
    private static final String ERROR_DUPLICATE_QUIZ = "Attempting to add a duplicate quiz.";
    private static final String ERROR_INVALID_QUIZ_ID = "Invalid new quiz ID";

    private static final String ERROR_SAVING_EMPTY_QUIZ = "Warning: not saving empty quizzes.";
    private static final String ERROR_SAVING_EMPTY_RETAKES = "Warning: not saving empty retakes.";

    public ScrollPane scrollPaneQuizzes;
    public ListView<QuizBean> listViewQuizzes;
    public ScrollPane scrollPaneRetakes;
    public Button buttonSave;
    public Button buttonRevertChanges;
    public Button buttonDeleteSelectedQuizzes;

    // Quiz form data
    public TextField textFieldQuizID;
    public DatePicker datePickerQuizDate;
    public TextField textFieldQuizHour;
    public TextField textFieldQuizMinute;
    public Button buttonClearRetakes;
    public Button buttonClearQuizzes;
    public LocalDate selectedQuizDate;

    public Button buttonAddQuiz;
    public Button buttonDeleteSelectedRetakes;
    public ListView<RetakeBean> listViewRetakes;

    // Retake form data
    public TextField textFieldRetakeID;
    public TextField textFieldRetakeLocation;
    public DatePicker datePickerRetakeDate;
    public TextField textFieldRetakeHour;
    public TextField textFieldRetakeMinute;
    public LocalDate selectedRetakeDate;

    public Button buttonAddRetake;

    public Text feedbackText;

    public HashSet<QuizBean> selectedQuizzes;
    public HashSet<RetakeBean> selectedRetakes;

    /**
     * Automatically called after the controller is instantiated.
     */
    @FXML
    public void initialize() {
        initViews();
        String courseID = Config.getInstance().getCourseID();
        if (courseID != null) {
            readDataFiles(courseID);
        }
    }

    private void readDataFiles(String courseID) {
        RetakesReader retakesReader = new RetakesReader();
        QuizReader quizReader = new QuizReader();

        Quizzes quizzes = null;

        String notFoundWarning = "";

        try {
            quizzes = quizReader.read(Config.getQuizzesFilename(courseID));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            notFoundWarning += "Warning: did not find an existing quiz XML file, creating a new one.\n";
        }

        Retakes retakes = null;
        try {
            retakes = retakesReader.read(Config.getRetakesFilename(courseID));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            notFoundWarning += "Warning: did not find an existing retakes XML file, creating a new one.\n";
        }

        Common.updateText(feedbackText, notFoundWarning);

        listViewQuizzes.setItems(
                quizzes == null ?
                    FXCollections.observableArrayList(new Quizzes().getItems()) :
                    FXCollections.observableArrayList(quizzes.getItems())
        );

        listViewRetakes.setItems(
                retakes == null ?
                    FXCollections.observableArrayList(new Retakes().getItems()) :
                    FXCollections.observableArrayList(retakes.getItems())
        );

        selectedRetakes = new HashSet<>();
        new CheckboxListener<RetakeBean>().setCheckBoxListener(selectedRetakes, listViewRetakes);

        selectedQuizzes = new HashSet<>();
        new CheckboxListener<QuizBean>().setCheckBoxListener(selectedQuizzes, listViewQuizzes);

    }

    private void initViews() {

        scrollPaneQuizzes.widthProperty().addListener((obs, oldVal, newVal) -> {
            listViewQuizzes.setPrefWidth(newVal.doubleValue());
            listViewQuizzes.setPrefWidth(newVal.doubleValue());
        });


        scrollPaneRetakes.widthProperty().addListener((obs, oldVal, newVal) -> {
            listViewRetakes.setPrefWidth(newVal.doubleValue());
            listViewRetakes.setPrefWidth(newVal.doubleValue());
        });


        datePickerQuizDate.setOnAction((e) -> selectedQuizDate = datePickerQuizDate.getValue());

        datePickerRetakeDate.setOnAction((e) -> selectedRetakeDate = datePickerRetakeDate.getValue());

    }

    public void onClickButtonBack() {
        try {
            Main.switchScene(Layouts.SCHEDULE, getClass(), 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickButtonSave() {
        String feedbackText = "";

        // Save quizzes
        if (listViewQuizzes.getItems().size() == 0) {
            feedbackText += ERROR_SAVING_EMPTY_QUIZ + "\n";
        }
        else {
            String quizPath = Config.getQuizzesFilename(Config.getInstance().getCourseID());
            QuizWriter quizWriter = new QuizWriter(quizPath);
            Quizzes quizzes = new Quizzes();
            listViewQuizzes.getItems().forEach(quizzes::addQuiz);

            boolean writeSuccess = true;
            try {
                quizWriter.write(quizzes);
            } catch (TransformerException | ParserConfigurationException e) {
                feedbackText += "Unable to write quizzes in " + quizPath + "\n";
                writeSuccess = false;
            }

            if (writeSuccess) {
                feedbackText += "Successfully saved " + quizPath + "\n";
            }

        }
        // Save retakes
        if (listViewRetakes.getItems().size() == 0) {
            feedbackText += ERROR_SAVING_EMPTY_RETAKES;
        }
        else {
            String retakesPath = Config.getRetakesFilename(Config.getInstance().getCourseID());
            RetakesWriter retakesWriter = new RetakesWriter(retakesPath);
            Retakes retakes = new Retakes();
            listViewRetakes.getItems().forEach(retakes::addRetake);

            boolean writeSuccess = true;
            try {
                retakesWriter.write(retakes);
            } catch (TransformerException | ParserConfigurationException e) {
                writeSuccess = false;
                feedbackText += "Unable to write retakes in " + retakesPath;
            }

            if (writeSuccess) {
                feedbackText += "Successfully saved " + retakesPath;
            }
        }

        Common.updateText(this.feedbackText, feedbackText);

    }

    public void onClickButtonRevertChanges() {
        readDataFiles(Config.getInstance().getCourseID());
    }

    public void onClickButtonDeleteSelectedQuizzes() {
        ObservableList<QuizBean> currentItems = listViewQuizzes.getItems();
        currentItems.removeAll(selectedQuizzes);
    }

    public void onClickButtonAddQuiz() {
        if(isNewQuizValid()){
            QuizBean newQuiz = new QuizBean(
                    Integer.parseInt(textFieldQuizID.getText()),
                    selectedQuizDate.getMonthValue(),
                    selectedQuizDate.getDayOfMonth(),
                    Integer.parseInt(textFieldQuizHour.getText()),
                    Integer.parseInt(textFieldQuizMinute.getText())
            );
            ObservableList<QuizBean> currentQuizzes = listViewQuizzes.getItems();
            if (currentQuizzes.contains(newQuiz)) {
                Common.updateText(feedbackText, ERROR_DUPLICATE_QUIZ);
            }
            else {
                Common.updateText(feedbackText);
                currentQuizzes.add(newQuiz);
            }
        }
    }

    private boolean isNewQuizValid() {
        Common.updateText(feedbackText);
        if (textFieldQuizID.getText().isEmpty() ||
            textFieldQuizHour.getText().isEmpty() ||
            selectedQuizDate == null ||
            textFieldQuizMinute.getText().isEmpty()) {
            Common.updateText(feedbackText, ERROR_QUIZ_MISSING_DATA);
            return false;
        }

        try {
            Integer.parseInt(textFieldQuizID.getText());
        } catch (NumberFormatException e) {
            Common.updateText(feedbackText, ERROR_INVALID_QUIZ_ID);
            return false;
        }

        if (invalidHourMinute(textFieldQuizHour.getText(), textFieldQuizMinute.getText())) {
            Common.updateText(feedbackText, ERROR_INVALID_TIME_QUIZ);
            return false;
        }

        return true;
    }

    /**
     * Checks if the hourString and minuteString is invalid.
     * @param hourString String representing the hour.
     * @param minuteString String representing the minute
     * @return true if the hourString and minuteString are invalid
     */
    private boolean invalidHourMinute(String hourString, String minuteString) {
        try {
            int hour = Integer.parseInt(hourString);
            int minute = Integer.parseInt(minuteString);
            if (hour >= 24 || hour < 0 || minute >= 60 || minute < 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    public void onClickButtonDeleteSelectedRetakes() {
        ObservableList<RetakeBean> currentItems = listViewRetakes.getItems();
        currentItems.removeAll(selectedRetakes);
    }

    private boolean isNewRetakeValid() {
        Common.updateText(feedbackText);
        if (textFieldRetakeID.getText().isEmpty() ||
            textFieldRetakeLocation.getText().isEmpty() ||
            textFieldRetakeHour.getText().isEmpty() ||
            textFieldRetakeMinute.getText().isEmpty() ||
            selectedRetakeDate == null) {
            Common.updateText(feedbackText, ERROR_RETAKE_MISSING_DATA);
            return false;
        }

        try {
            Integer.parseInt(textFieldRetakeID.getText());
        } catch (NumberFormatException e) {
            Common.updateText(feedbackText, ERROR_INVALID_RETAKE_ID);
            return false;
        }


        if (invalidHourMinute(textFieldRetakeHour.getText(), textFieldRetakeMinute.getText())) {
            Common.updateText(feedbackText, ERROR_INVALID_TIME_RETAKE);
            return false;
        }

        return true;
    }

    public void onClickButtonAddRetake() {
        if (isNewRetakeValid()) {
            ObservableList<RetakeBean> currentRetakes = listViewRetakes.getItems();

            RetakeBean newRetake = new RetakeBean(
                Integer.parseInt(textFieldRetakeID.getText()),
                textFieldRetakeLocation.getText(),
                selectedRetakeDate.getMonthValue(),
                selectedRetakeDate.getDayOfMonth(),
                Integer.parseInt(textFieldRetakeHour.getText()),
                Integer.parseInt(textFieldRetakeMinute.getText())
            );

            if (currentRetakes.contains(newRetake)) {
                Common.updateText(feedbackText, ERROR_DUPLICATE_RETAKE);
            }

            else {
                Common.updateText(feedbackText);
                currentRetakes.add(newRetake);
            }
        }
    }

    public void onClickButtonClearRetakes() {
        listViewRetakes.getItems().clear();
    }

    public void onClickButtonClearQuizzes() {
        listViewQuizzes.getItems().clear();
    }
}
