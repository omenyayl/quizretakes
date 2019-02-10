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

import static quizretakes.utils.QuizXMLFile.separator;

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

    private static final String ERROR_NO_DATA_FILE = "Error 101: Cannot find data files";
    private static final String ERROR_NO_NAME = "Error 102: Name is blank";
    private static final String ERROR_NO_QUIZZES_SELECTED = "Error 103: No quizzes selected";
    private static final String ERROR_FAILED_SAVE = "Error 104: Could not save appointment";
    private static final String ERROR_NO_APPTS_FILE = "Error 105: Did not find the appointments file";

    private HashSet<QuizListItem> selectedQuizzes;

    /**
     * Automatically called after the controller is instantiated.
     */
    @FXML
    public void initialize() {
        initViews();
        if (Main.pCourseID != null) {
            readDataFiles(Main.pCourseID);
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
        textCourseID.setText(Main.pCourseID);
    }

    /**
     * Reads data files with respect to the course ID
     * @param courseID the ID of the course, e.g. swe437
     */
    void readDataFiles(String courseID) {
        CourseBean course;
        CourseReader cr = new CourseReader();

        courseFileName = QuizXMLFile.getCourseFilename(courseID);

        try {
            course = cr.read(courseFileName);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            String message = String.format("%s for %s", ERROR_NO_DATA_FILE, courseID);
            textError.setText(message);
            textError.setVisible(true);
            return;
        }

        String quizzesFilename = QuizXMLFile.getQuizzesFilename(courseID);
        String retakesFilename = QuizXMLFile.getRetakesFilename(courseID);

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

        String apptsFileName = QuizXMLFile.getApptsFilename(Main.pCourseID);

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
            textError.setText(ERROR_FAILED_SAVE);
            textError.setVisible(true);
        }

    }

    public void onClickButtonBack(ActionEvent actionEvent) {
        try {
            Main.switchScene("/layouts/login.fxml", getClass(), 300, 300);
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
            textError.setVisible(false);
            submitData();
        }
        else {
            textError.setText(errorText);
            textError.setVisible(true);
            textSuccess.setVisible(false);
        }
    }

    public void onClickButtonRefresh(ActionEvent actionEvent) {
        textError.setVisible(false);
        textSuccess.setVisible(false);
        readDataFiles(Main.pCourseID);
    }

    public void onClickButtonAppointments(ActionEvent actionEvent) {

        File courseDir = new File(
                String.join(
                        System.getProperty("user.dir"),
                        QuizXMLFile.getApptsFilename(Main.pCourseID))
        );

        if (courseDir.exists()) {
            try {
                Main.switchScene("/layouts/appointments.fxml", getClass(), 800, 600);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            textError.setText(ERROR_NO_APPTS_FILE);
            textError.setVisible(true);
            textSuccess.setVisible(false);
        }
    }
}
