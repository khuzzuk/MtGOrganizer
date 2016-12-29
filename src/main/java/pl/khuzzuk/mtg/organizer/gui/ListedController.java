package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pl.khuzzuk.dao.Named;
import pl.khuzzuk.messaging.Bus;

import java.util.Collection;
import java.util.Properties;
import java.util.function.Consumer;

abstract class ListedController<T extends Named<String>> implements InitController {
    Bus bus;
    Properties messages;
    Consumer<T> selectAction;
    Consumer<T> saveAction;
    Consumer<T> deleteAction;
    @FXML
    ListView<T> items;
    @FXML
    TextField name;

    ListedController(Bus bus, Properties messages) {
        this.bus = bus;
        this.messages = messages;
        selectAction = this::load;
        saveAction = this::saveElement;
        deleteAction = this::deleteElement;
    }

    void load(T t) {
        name.setText(t.getName());
    }

    abstract void saveElement(T t);

    abstract T createElement();

    abstract void deleteElement(T t);

    @FXML
    void clear() {
        name.textProperty().unbind();
        name.clear();
    }

    @FXML
    private void select() {
        process(selectAction);
    }

    @FXML
    private void save() {
        if (name.getText().length() > 2) {
            saveAction.accept(createElement());
        }
    }

    public void delete() {
        process(deleteAction);
    }

    private void process(Consumer<T> action) {
        T t = getSelectedItem();
        if (t != null && t.getName().length() > 2) {
            action.accept(t);
        }
    }

    public void loadAll(Collection<T> elements) {
        items.getItems().clear();
        items.getItems().addAll(elements);
    }

    T getSelectedItem() {
        return items.getSelectionModel().getSelectedItem();
    }
}
