package pl.khuzzuk.mtg.organizer.gui;

import javafx.scene.control.ComboBox;

import java.util.Collection;

class ComboBoxHandler {
    static <T> void fill(Collection<? extends T> elements, ComboBox<T> comboBox) {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(elements);
    }

    static String getEmptyIfNotPresent(ComboBox<String> list) {
        String value = list.getSelectionModel().getSelectedItem();
        return value != null ? value : "";
    }

    static <T> void selectOrEmpty(ComboBox<T> box, T element) {
        if (box.getItems().contains(element)) {
            box.getSelectionModel().select(element);
        } else {
            box.getSelectionModel().clearSelection();
        }
    }

    static <T> T getOrNull(ComboBox<T> comboBox) {
        return comboBox.getSelectionModel().getSelectedItem();
    }
}
