package quizretakes;

import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneFactory {
    public static Scene buildBootstrapScene(Parent root, Class<?> c) {
        Scene scene = new Scene(root, 300, 275);
        scene.getStylesheets().add(c.getResource("/bootstrap3.css").toExternalForm());
        return scene;
    }
}
