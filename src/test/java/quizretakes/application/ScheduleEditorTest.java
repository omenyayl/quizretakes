package quizretakes.application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.xml.sax.SAXException;
import quizretakes.Layouts;
import quizretakes.utils.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ScheduleEditorTest extends ApplicationTest {
    private Scene scene;

    private final String quizzesXMLPath = "./quiz-orig-swe437.xml";

    @Before
    public void setUp() {

    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Layouts.SCHEDULE_EDITOR.toString()));
        scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();
    }

    @Test
    public void setupTest(){}

    @Test
    public void showListOfQuizzes() {
        ListView quizList = (ListView) scene.lookup("#listViewQuizzes");

        Quizzes quizzes = new Quizzes();
        try {
            QuizReader quizReader = new QuizReader();
            quizzes = quizReader.read(quizzesXMLPath);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }


}
