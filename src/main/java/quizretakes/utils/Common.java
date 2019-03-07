package quizretakes.utils;

import javafx.scene.text.Text;

public class Common {
    public static void updateText(Text text, String...errors) {
        if (errors.length != 0 && !errors[0].isEmpty()) {
            text.setVisible(true);
            text.setText(String.join("\n", errors));
        }
        else {
            text.setVisible(false);
        }
    }

}
