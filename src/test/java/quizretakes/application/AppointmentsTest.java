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
import quizretakes.Layouts;
import quizretakes.utils.Config;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxToolkit.registerPrimaryStage;

public class AppointmentsTest extends ApplicationTest {

    private Scene scene;

    @BeforeClass
    public static void setupSpec() throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
        registerPrimaryStage();
    }

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
        Config.getInstance().setCourseID("swe437");

        Parent root = FXMLLoader.load(getClass().getResource(Layouts.APPOINTMENTS.toString()));
        scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/bootstrap3.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.toFront();

    }

    @Test
    public void testButtonReload () {

        ListView listView = lookup("#listViewAppointments").queryListView();

        String listBefore = listView.getItems().toString();

        clickOn("#buttonReload");

        String listAfter = listView.getItems().toString();

        assertEquals(listBefore, listAfter);

    }


}