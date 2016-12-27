package pl.khuzzuk.mtg.organizer.gui;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.dao.Named;
import pl.khuzzuk.messaging.Bus;

import java.util.Properties;
import java.util.function.Consumer;

abstract class ListedController<T extends Named<String>> implements InitController {
    Bus bus;
    DAO dao;
    Properties messages;
    Consumer<String> selectAction;
    T item;
    @FXML
    ListView<T> items;
    @FXML
    TextField name;

    public ListedController(Bus bus, DAO dao, Properties messages) {
        this.bus = bus;
        this.dao = dao;
        this.messages = messages;
    }

    abstract public void load();

    void clear() {
        name.textProperty().unbind();
        name.clear();
    }

    private void select() {
        selectAction.accept(name.getText());
    }

    StringBinding getNameBinding() {
        StringBinding binding = new StringBinding() {
            @Override
            protected String computeValue() {
                return item.getName();
            }
        };
        binding.addListener((o, oldValue, newValue) -> item.setName(newValue));
        return binding;
    }
}
