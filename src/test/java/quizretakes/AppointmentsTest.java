package quizretakes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.*;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertEquals;

public class AppointmentsTest extends ApplicationTest {

    private Scene scene;

    @Before
    public void setUp() {
        Main.pCourseID = "swe437";
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Override
    public void start (Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Layouts.APPOINTMENTS.toString()));
        scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();

    }

    @Test
    public void testButtonReload () {

        ListView listView = (ListView) scene.lookup("#listViewAppointments");

        String listBefore = listView.getItems().toString();

        clickOn("#buttonReload");

        String listAfter = listView.getItems().toString();

        assertEquals(listBefore, listAfter);

    }

}