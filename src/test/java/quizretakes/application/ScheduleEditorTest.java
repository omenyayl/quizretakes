package quizretakes.application;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.xml.sax.SAXException;
import quizretakes.Layouts;
import quizretakes.controllers.ScheduleEditorController;
import quizretakes.utils.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ScheduleEditorTest extends ApplicationTest {

    private ScheduleEditorController controllerInstance;

    private static final String courseID = "testCourse";

    @BeforeClass
    public static void beforeClass() {
        writeMockXMLData();
    }

    private static Quizzes getQuizzes() {
        Quizzes quizzes = new Quizzes();
        quizzes.addQuiz(new QuizBean(
                1, 2, 3, 10, 20
        ));
        quizzes.addQuiz(new QuizBean(
                2, 3, 4, 10, 0
        ));
        quizzes.addQuiz(new QuizBean(
                3, 1, 20, 9, 30
        ));
        return quizzes;
    }

    private static Retakes getRetakes() {
        Retakes retakes = new Retakes();
        retakes.addRetake(
                new RetakeBean(
                        1, "ENGR 2313", 3, 5, 10, 30
                )
        );

        retakes.addRetake(
                new RetakeBean(
                        2, "ENGR 3321", 3, 10, 9, 30
                )
        );

        retakes.addRetake(
                new RetakeBean(
                        3, "ENGR 1105", 3, 10, 11, 30
                )
        );
        return retakes;
    }

    private static void writeMockXMLData() {
        Config.getInstance().setCourseID(courseID);

        // Create mock data
        QuizWriter quizWriter = new QuizWriter(Config.getQuizzesFilename(courseID));
        try {
            quizWriter.write(getQuizzes());
        } catch (TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
            fail("Could not write quizzes");
        }

        RetakesWriter retakesWriter =
                new RetakesWriter(Config.getRetakesFilename(courseID));
        try {
            retakesWriter.write(getRetakes());
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            fail("Could not write retakes");
        }
    }

    @Before
    public void setUp() {
        writeMockXMLData();
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        File quizzesFile = new File(Config.getQuizzesFilename(courseID));
        if (quizzesFile.exists()) {
            quizzesFile.deleteOnExit();
        }

        File retakesFile = new File(Config.getRetakesFilename(courseID));
        if (retakesFile.exists()) {
            retakesFile.deleteOnExit();
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Layouts.SCHEDULE_EDITOR.toString()));
        Parent root = loader.load();
        controllerInstance = loader.getController();

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();
    }

    @Test
    public void setupTest() {
    }

    /**
     * Checks that the user is able to see the list of active quizzes.
     */
    @Test
    public void showListOfQuizzes() {
        ArrayList<QuizBean> quizBeanArrayList = getQuizzes().getItems();
        assertEquals(quizBeanArrayList.toString(), controllerInstance.listViewQuizzes.getItems().toString());
    }

    /**
     * Checks that the user is able to see the list of active retakes.
     */
    @Test
    public void showListOfRetakes() {
        ArrayList<RetakeBean> retakeBeanArrayList = getRetakes().getItems();
        assertEquals(retakeBeanArrayList.toString(), controllerInstance.listViewRetakes.getItems().toString());
    }

    /**
     * Check to see if a list of selected quizzes is instantiated
     */
    @Test
    public void quizzesHaveSelections() {
        assertNotNull(controllerInstance.selectedQuizzes);
    }

    /**
     * Check to see if a list of selected quizzes is instantiated
     */
    @Test
    public void retakesHaveSelections() {
        assertNotNull(controllerInstance.selectedRetakes);
    }

    /**
     * Tests if the program can delete a selected quiz
     */
    @Test
    public void testQuizDelete() {
        controllerInstance.selectedQuizzes.add(
                controllerInstance.listViewQuizzes.getItems().get(0)
        );
        clickOn(controllerInstance.buttonDeleteSelectedQuizzes);

        ArrayList<QuizBean> correctQuizzes = getQuizzes().getItems();
        correctQuizzes.remove(0);

        assertEquals(correctQuizzes.toString(), controllerInstance.listViewQuizzes.getItems().toString());
    }

    /**
     * Tests if the program can delete the first and the last quiz
     */
    @Test
    public void testQuizDelete2() {
        ObservableList<QuizBean> quizList = controllerInstance.listViewQuizzes.getItems();
        controllerInstance.selectedQuizzes.add(
                quizList.get(0)
        );
        controllerInstance.selectedQuizzes.add(
                quizList.get(quizList.size() - 1)
        );
        clickOn(controllerInstance.buttonDeleteSelectedQuizzes);

        ArrayList<QuizBean> correctQuizzes = getQuizzes().getItems();
        correctQuizzes.remove(0);
        correctQuizzes.remove(correctQuizzes.size()- 1);

        assertEquals(correctQuizzes.toString(), controllerInstance.listViewQuizzes.getItems().toString());
    }

    /**
     * Tests if the program can delete the first retake
     */
    @Test
    public void testRetakeDelete() {
        controllerInstance.selectedRetakes.add(
                controllerInstance.listViewRetakes.getItems().get(0)
        );
        clickOn(controllerInstance.buttonDeleteSelectedRetakes);

        ArrayList<RetakeBean> correctRetakes = getRetakes().getItems();
        correctRetakes.remove(0);

        assertEquals(correctRetakes.toString(), controllerInstance.listViewRetakes.getItems().toString());
    }

    /**
     * Tests whether the program can delete the first and last retake
     */
    @Test
    public void testRetakeDelete2() {
        ObservableList<RetakeBean> retakeList = controllerInstance.listViewRetakes.getItems();
        controllerInstance.selectedRetakes.add(
                retakeList.get(0)
        );
        controllerInstance.selectedRetakes.add(
                retakeList.get(retakeList.size() - 1)
        );
        clickOn(controllerInstance.buttonDeleteSelectedRetakes);

        ArrayList<RetakeBean> correctRetakes = getRetakes().getItems();
        correctRetakes.remove(0);
        correctRetakes.remove(correctRetakes.size()- 1);

        assertEquals(correctRetakes.toString(), controllerInstance.listViewRetakes.getItems().toString());
    }

    /**
     * Can add a quiz
     */
    @Test
    public void testQuizAdd() {
        clickOn(controllerInstance.textFieldQuizID);
        write("123");
        clickOn(controllerInstance.textFieldQuizHour);
        write("10");
        clickOn(controllerInstance.textFieldQuizMinute);
        write("30");
        controllerInstance.selectedQuizDate = LocalDate.of(2019, 3, 5);
        clickOn(controllerInstance.buttonAddQuiz);
        Quizzes expectedQuizzes = getQuizzes();
        expectedQuizzes.addQuiz(
                new QuizBean(123, 3, 5, 10, 30)
        );

        assertEquals(expectedQuizzes.toString(), controllerInstance.listViewQuizzes.getItems().toString());
    }

    /**
     * Can add 2 quizzes
     */
    @Test
    public void testQuizAdd2() {
        doubleClickOn(controllerInstance.textFieldQuizID);
        write("123");
        doubleClickOn(controllerInstance.textFieldQuizHour);
        write("10");
        doubleClickOn(controllerInstance.textFieldQuizMinute);
        write("30");
        controllerInstance.selectedQuizDate = LocalDate.of(2019, 3, 5);
        clickOn(controllerInstance.buttonAddQuiz);

        doubleClickOn(controllerInstance.textFieldQuizID);
        write("1234");
        doubleClickOn(controllerInstance.textFieldQuizHour);
        write("2");
        doubleClickOn(controllerInstance.textFieldQuizMinute);
        write("15");
        controllerInstance.selectedQuizDate = LocalDate.of(2019, 4, 5);
        clickOn(controllerInstance.buttonAddQuiz);

        Quizzes expectedQuizzes = getQuizzes();
        expectedQuizzes.addQuiz(
                new QuizBean(123, 3, 5, 10, 30)
        );
        expectedQuizzes.addQuiz(
                new QuizBean(1234, 4, 5, 2, 15)
        );

        assertEquals(expectedQuizzes.toString(), controllerInstance.listViewQuizzes.getItems().toString());
    }

    /**
     * Can add a retake
     */
    @Test
    public void testRetakeAdd() {
        Retakes retakeBeans = getRetakes();
        retakeBeans.addRetake(new RetakeBean(10, "ENGR 1241", 2, 3, 4, 15));

        doubleClickOn(controllerInstance.textFieldRetakeID);
        write("10");
        doubleClickOn(controllerInstance.textFieldRetakeLocation);
        write("ENGR 1241");
        controllerInstance.selectedRetakeDate = LocalDate.of(2019, 2, 3);
        doubleClickOn(controllerInstance.textFieldRetakeHour);
        write("4");
        doubleClickOn(controllerInstance.textFieldRetakeMinute);
        write("15");
        clickOn(controllerInstance.buttonAddRetake);

        assertEquals(retakeBeans.getItems().toString(), controllerInstance.listViewRetakes.getItems().toString());
    }

    /**
     * Checks if the program displays an error and skips adding a quiz with missing information
     */
    @Test
    public void testQuizAddEmpty () {
        Quizzes quizzes = getQuizzes();
        clickOn(controllerInstance.buttonAddQuiz);
        assertEquals(quizzes.getItems().toString(), controllerInstance.listViewQuizzes.getItems().toString());
        assertTrue(controllerInstance.feedbackText.isVisible());
    }

    /**
     * Checks if the program displays an error and skips adding a quiz with the same ID.
     */
    @Test
    public void testQuizSameID () {
        Quizzes quizzes = getQuizzes();

        doubleClickOn(controllerInstance.textFieldQuizID);
        write("1");
        doubleClickOn(controllerInstance.textFieldQuizHour);
        write("12");
        doubleClickOn(controllerInstance.textFieldQuizMinute);
        write("30");
        controllerInstance.selectedQuizDate = LocalDate.of(2019, 3, 5);
        clickOn(controllerInstance.buttonAddQuiz);

        assertEquals(quizzes.getItems().toString(), controllerInstance.listViewQuizzes.getItems().toString());
        assertTrue(controllerInstance.feedbackText.isVisible());
    }

    /**
     * Verifies that an error is shown when attempting to add an empty retake
     */
    @Test
    public void testRetakeAddEmpty() {
        Retakes retakeBeans = getRetakes();
        clickOn(controllerInstance.buttonAddRetake);
        assertEquals(retakeBeans.getItems().toString(), controllerInstance.listViewRetakes.getItems().toString());
        assertTrue(controllerInstance.feedbackText.isVisible());
    }

    /**
     * Checks if the program displays an error and skips adding a retake with the same ID.
     */
    @Test
    public void testRetakeAddSameID () {
        Retakes retakeBeans = getRetakes();

        doubleClickOn(controllerInstance.textFieldRetakeID);
        write("1");
        doubleClickOn(controllerInstance.textFieldRetakeLocation);
        write("ENGR 1241");
        controllerInstance.selectedRetakeDate = LocalDate.of(2019, 2, 3);
        doubleClickOn(controllerInstance.textFieldRetakeHour);
        write("4");
        doubleClickOn(controllerInstance.textFieldRetakeMinute);
        write("15");
        clickOn(controllerInstance.buttonAddRetake);

        assertTrue(controllerInstance.feedbackText.isVisible());
        assertEquals(retakeBeans.getItems().toString(), controllerInstance.listViewRetakes.getItems().toString());

    }

    /**
     * Checks if both quizzes and retakes are saved to an XML file
     */
    @Test
    public void testSave() {
        File quizzesFile = new File(Config.getQuizzesFilename(courseID));
        File retakesFile = new File(Config.getRetakesFilename(courseID));

        if (quizzesFile.exists()) {
            assertTrue("quizzes file should had been deleted before the save test",
                    quizzesFile.delete());
        }
        if (retakesFile.exists()) {
            assertTrue("retakes file should had been deleted before the save test",
                    retakesFile.delete());
        }

        clickOn(controllerInstance.buttonSave);

        assertTrue(quizzesFile.exists());
        assertTrue(retakesFile.exists());
    }

    /**
     * Checks if the program saved quizzes and retakes after editing them
     */
    @Test
    public void testSaveEdited() throws ParserConfigurationException, SAXException, IOException {
        Quizzes quizzes = getQuizzes();
        quizzes.addQuiz(new QuizBean(
                13, 5, 2, 4, 20
        ));

        Retakes retakeBeans = getRetakes();
        retakeBeans.addRetake(new RetakeBean(
                15, "ENGR 3476", 5, 6,  9, 35
        ));

        doubleClickOn(controllerInstance.textFieldQuizID);
        write("13");
        controllerInstance.selectedQuizDate = LocalDate.of(2019, 5, 2);
        doubleClickOn(controllerInstance.textFieldQuizHour);
        write("4");
        doubleClickOn(controllerInstance.textFieldQuizMinute);
        write("20");
        clickOn(controllerInstance.buttonAddQuiz);

        doubleClickOn(controllerInstance.textFieldRetakeID);
        write("15");
        doubleClickOn(controllerInstance.textFieldRetakeLocation);
        write("ENGR 3476");
        doubleClickOn(controllerInstance.textFieldRetakeHour);
        write("9");
        doubleClickOn(controllerInstance.textFieldRetakeMinute);
        write("35");
        controllerInstance.selectedRetakeDate = LocalDate.of(2019, 5, 6);
        clickOn(controllerInstance.buttonAddRetake);

        clickOn(controllerInstance.buttonSave);

        Retakes readRetakes = new RetakesReader().read(
                Config.getRetakesFilename(courseID));
        Quizzes readQuizzes = new QuizReader().read(
                Config.getQuizzesFilename(courseID));

        assertEquals(retakeBeans.toString(), readRetakes.toString());
        assertEquals(quizzes.toString(), readQuizzes.toString());

    }



}
