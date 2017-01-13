package pl.khuzzuk.mtg.organizer.gui;

import javafx.scene.control.ListCell;

public class FilterCell<T> extends ListCell<FilterOption<T>> {

    @Override
    protected void updateItem(FilterOption<T> item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null || !empty) {
            item.setView(this);
        }
    }
}
