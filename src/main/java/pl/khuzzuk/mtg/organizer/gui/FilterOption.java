package pl.khuzzuk.mtg.organizer.gui;

import javafx.scene.control.ButtonBase;
import javafx.scene.control.ListCell;

import java.util.function.Predicate;

interface FilterOption<T> {
    Predicate<T> getPredicate();

    String getName();

    void setView(ListCell<FilterOption<T>> cell);

    ButtonBase getButton();
}
