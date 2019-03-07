package quizretakes.utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.CheckBoxListCell;

import java.util.HashSet;

public class CheckboxListener <E> {
    public void setCheckBoxListener(HashSet<E> selected, ListView<E> listView) {
//        if (selected == null) throw new IllegalArgumentException("HashSet selected cannot be null.");
        listView.setCellFactory(
                CheckBoxListCell.forListView(param -> {
                    BooleanProperty observable = new SimpleBooleanProperty();
                    observable.addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            selected.add(param);
                        } else {
                            selected.remove(param);
                        }
                    });
                    return observable;
                })
        );
    }
}
