package quizretakes.controllers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;
import quizretakes.Layouts;
import quizretakes.components.QuizListItem;
import quizretakes.Main;
import quizretakes.utils.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static quizretakes.utils.Config.separator;

public class ScheduleController {
    public TextField textFieldName;
    public Text textDates;
    public Text textCourseID;
    public ListView<String> listViewAllOpportunities;
    public ScrollPane scrollPaneAllOpportunities;
    public ScrollPane scrollPaneRequestRetakes;
    public ListView<QuizListItem> listViewRequestRetakes;
    public GridPane gridPaneRetakes;
    public Text textError;
    public Text textSuccess;

    private String courseFileName;
    private int daysAvailable = 14;

    private static final String ERROR_NO_COURSE_FILE = "Error 101: Cannot find the course file";
    private static final String ERROR_NO_NAME = "Error 102: Name is blank";
    private static final String ERROR_NO_QUIZZES_SELECTED = "Error 103: No quizzes selected";
    private static final String ERROR_FAILED_SAVE = "Error 104: Could not save appointment";
    private static final String ERROR_NO_APPTS_FILE = "Error 105: Did not find the appointments file";
    private static final String ERROR_NO_DATA_FILES = "Error 108: Cannot find retakes and/or quiz XML files";


    private HashSet<QuizListItem> selectedQuizzes;

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

    /**
     * Initializes views to dynamically resize with the window
     */
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

        textError.setWrappingWidth(Main.getStage().getWidth()*0.9);
        textCourseID.setText(Config.getInstance().getCourseID());
    }

    /**
     * Reads data files with respect to the course ID
     * @param courseID the ID of the course, e.g. swe437
     */
    void readDataFiles(String courseID) {
        CourseBean course;
        CourseReader cr = new CourseReader();

        courseFileName = Config.getCourseFilename(courseID);

        try {
            course = cr.read(courseFileName);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            String message = String.format("%s for %s", ERROR_NO_COURSE_FILE, courseID);
            updateErrors(message);
            return;
        }

        String quizzesFilename = Config.getQuizzesFilename(courseID);
        String retakesFilename = Config.getRetakesFilename(courseID);

        Quizzes quizList    = new Quizzes();
        Retakes retakesList = new Retakes();
        QuizReader qr = new QuizReader();
        RetakesReader rr = new RetakesReader();

        try {
            quizList    = qr.read (quizzesFilename);
            retakesList = rr.read (retakesFilename);
            updateData(quizList, retakesList, course);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
            updateErrors(ERROR_NO_DATA_FILES);
        }

        selectedQuizzes = new HashSet<>();

    }

    /**
     * Updates the ListView data with the data from Quizzes, Retakes, and CourseBean
     * @param quizList list of quiz data
     * @param retakesList list of retake data
     * @param course course data
     */
    private void updateData(Quizzes quizList, Retakes retakesList, CourseBean course){
        LocalDate startSkip = course.getStartSkip();
        LocalDate endSkip   = course.getEndSkip();

        LocalDate today  = LocalDate.now();
        LocalDate endDay = today.plusDays((long) daysAvailable);
        // if endDay is between startSkip and endSkip, add 7 to endDay
        if (!endDay.isBefore(startSkip) && !endDay.isAfter(endSkip))
        {  // endDay is in a skip week, add 7 to endDay
            endDay = endDay.plusDays(7L);
        }

        textDates.setText(
                String.format("%d/%d/%d - %d/%d/%d",
                        today.getMonthValue(),
                        today.getDayOfMonth(),
                        today.getYear(),
                        endDay.getMonthValue(),
                        endDay.getDayOfMonth(),
                        endDay.getYear())
        );

        ArrayList<String> allRetakes = new ArrayList<>(10);
        ArrayList<QuizListItem> quizzes = new ArrayList<>(10);

        for(RetakeBean r: retakesList)
        {
            allRetakes.add(r.toString());

            LocalDate retakeDay = r.getDate();
            if (!(retakeDay.isBefore (today)) && !(retakeDay.isAfter (endDay)))
            {
                for(QuizBean q: quizList)
                {
                    LocalDate quizDay = q.getDate();
                    LocalDate lastAvailableDay = quizDay.plusDays((long) daysAvailable);
                    // To retake a quiz on a given retake day, the retake day must be within two ranges:
                    // quizDay <= retakeDay <= lastAvailableDay --> (!quizDay > retakeDay) && !(retakeDay > lastAvailableDay)
                    // today <= retakeDay <= endDay --> !(today > retakeDay) && !(retakeDay > endDay)

                    if (!quizDay.isAfter(retakeDay) && !retakeDay.isAfter(lastAvailableDay) &&
                            !today.isAfter(retakeDay) && !retakeDay.isAfter(endDay))
                    {
                        quizzes.add(new QuizListItem(r.getID(),
                                q.getID(),
                                quizDay));
                    }
                }
            }
        }

        listViewAllOpportunities.setItems(
                FXCollections.observableArrayList(allRetakes)
        );

        listViewRequestRetakes.setItems(
                FXCollections.observableArrayList(quizzes)
        );

        listViewRequestRetakes.setCellFactory(
                CheckBoxListCell.forListView(param -> {
                    BooleanProperty observable = new SimpleBooleanProperty();
                    observable.addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            selectedQuizzes.add(param);
                        } else {
                            selectedQuizzes.remove(param);
                        }
                    });
                    return observable;
                })
        );

    }


    /**
     * Calls after the user clicks the submit button. This creates an appointment file that corresponds with the
     * user's selected appointments
     */
    void submitData() {

        String apptsFileName = Config.getApptsFilename(Config.getInstance().getCourseID());

        // Get name and list of retake requests from parameters
        String studentName = textFieldName.getText();

        List<String> allIDs = selectedQuizzes.stream()
                .map(QuizListItem::getIDString)
                .collect(Collectors.toList());

        // Append the new appointment to the file
        try {
            File file = new File(apptsFileName);
            if (!file.exists())
            {

                file.createNewFile();

            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true); //append mode
            BufferedWriter bw = new BufferedWriter(fw);

            for(String oneIDPair : allIDs)
            {
                bw.write(oneIDPair + separator + studentName + "\n");
            }

            bw.flush();
            bw.close();

            textSuccess.setVisible(true);
        } catch (IOException e) {
            updateErrors(ERROR_FAILED_SAVE);
        }

    }

    public void onClickButtonBack(ActionEvent actionEvent) {
        try {
            Main.switchScene(Layouts.LOGIN, getClass(), 300, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClickButtonSubmitRequest(ActionEvent actionEvent) {
        String errorText = "";
        if (textFieldName.getText().isEmpty()) {
            errorText += ERROR_NO_NAME + "\n";
        }
        if (selectedQuizzes.size() == 0) {
            errorText += ERROR_NO_QUIZZES_SELECTED + "\n";
        }
        if (errorText.isEmpty()) {
            updateErrors();
            submitData();
        }
        else {
            updateErrors(errorText);
            textSuccess.setVisible(false);
        }
    }

    public void onClickButtonRefresh(ActionEvent actionEvent) {
        updateErrors();
        textSuccess.setVisible(false);
        readDataFiles(Config.getInstance().getCourseID());
    }

    public void onClickButtonAppointments(ActionEvent actionEvent) {

        File courseDir = new File(
                String.join(
                        System.getProperty("user.dir"),
                        Config.getApptsFilename(Config.getInstance().getCourseID()))
        );

        if (courseDir.exists()) {
            try {
                Main.switchScene(Layouts.APPOINTMENTS, getClass(), 800, 600);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            updateErrors(ERROR_NO_APPTS_FILE);
            textSuccess.setVisible(false);
        }
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
