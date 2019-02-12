package quizretakes.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;
import quizretakes.Layouts;
import quizretakes.Main;
import quizretakes.utils.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentsController {

    public ScrollPane scrollPaneAppointments;
    public ListView<String> listViewAppointments;
    public Text textError;


    private static final String ERROR_CANNOT_READ_APPOINTMENTS = "Error 106: Cannot find/read appointments file.";
    private static final String ERROR_CANNOT_READ_RETAKES = "Error 107: Cannot find/read retakes data file.";


    /**
     * Initializes the views to resize dynamically
     */
    private void initViews() {
        scrollPaneAppointments.widthProperty().addListener((obs, oldVal, newVal) -> {
            listViewAppointments.setPrefWidth(newVal.doubleValue());
        });

    }

    /**
     * Called after this controller is instantiated
     */
    @FXML
    public void initialize() {
        initViews();
        initData();
    }

    /**
     * Reads the retake and appointment data
     */
    private void initData() {
        HashMap<Integer, RetakeBean> retakeBeanHashMap = readDateTimeLocations(QuizXMLFile.getRetakesFilename(Main.pCourseID));
        ArrayList<ApptBean> appts = readAppointmentsData(QuizXMLFile.getApptsFilename(Main.pCourseID));
        updateListData(retakeBeanHashMap, appts);
    }

    /**
     * reads the retake XML data
     * @param retakesFile path to the retakes XML data file
     * @return HashMap that contains a RetakeBean value for each corresponding retake ID
     */
    private HashMap<Integer, RetakeBean> readDateTimeLocations(String retakesFile) {
        HashMap<Integer, RetakeBean> retakesMap = new HashMap<>();
        try {
            Retakes retakes = new RetakesReader().read(retakesFile);

            for (RetakeBean retake : retakes) {
                retakesMap.put(retake.getID(), retake);
            }

            return retakesMap;

        } catch (IOException | ParserConfigurationException | SAXException e) {
            updateErrors(ERROR_CANNOT_READ_RETAKES);
            e.printStackTrace();
        }

        return null;

    }

    /**
     * Reads the appointments TXT file
     * @param appointmentsFile Path to the appointments XML file
     */
    private ArrayList<ApptBean> readAppointmentsData(String appointmentsFile) {
        ArrayList<ApptBean> appts = null;
        try {
            appts = new ApptsReader().read(appointmentsFile);
        } catch (IOException e) {
            updateErrors(ERROR_CANNOT_READ_APPOINTMENTS);
            e.printStackTrace();
        }
        return appts;
    }

    /**
     * Updates the appointments ListView using the read appointment data
     * @param appts Read appointments data
     */
    private void updateListData(HashMap<Integer, RetakeBean> retakeBeanHashMap, ArrayList<ApptBean> appts) {
        List<String> appointments;

        appointments = appts.stream()
                .map((appointment) -> {
                    RetakeBean r = null;
                    if (retakeBeanHashMap != null) {
                        r = retakeBeanHashMap.get(appointment.getRetakeID());
                    }
                    if (r == null) {
                        return String.format("Session ID: %d - Quiz ID: %d - Name: %s",
                        appointment.getRetakeID(),
                        appointment.getQuizID(),
                        appointment.getName());
                    }
                    else {
                        LocalDate retakeDate = r.getDate();
                        String date = String.format("%s/%s/%s",
                                retakeDate.getMonthValue(),
                                retakeDate.getDayOfMonth(),
                                retakeDate.getYear());
                        return String.format("Session %d - Quiz %d - %s - %s - %s - %s",
                                appointment.getRetakeID(),
                                appointment.getQuizID(),
                                date,
                                r.timeAsString(),
                                r.getLocation(),
                                appointment.getName());
                    }
                }).collect(Collectors.toList());
        appointments.sort(String::compareTo);
        listViewAppointments.setItems(FXCollections.observableArrayList(appointments));
    }

    public void onClickButtonBack(ActionEvent actionEvent) throws IOException {
        Main.switchScene(Layouts.SCHEDULE, getClass(), 800, 600);
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

    public void onClickButtonReload(ActionEvent actionEvent) {
        initData();
    }
}
