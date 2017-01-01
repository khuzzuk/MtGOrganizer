package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import pl.khuzzuk.messaging.Bus;

import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;

public abstract class ChooserController<T> implements InitController {
    Bus bus;
    Properties messages;
    Stage parent;
    String endMessage;
    @FXML
    ListView<T> items;

    public ChooserController(Bus bus, Properties messages) {
        this.bus = bus;
        this.messages = messages;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        items.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    void load(Collection<T> types) {
        items.getItems().clear();
        items.getItems().addAll(types);
        parent.show();
    }

    @FXML
    abstract void choose();

    void setParent(Stage parent) {
        this.parent = parent;
    }
}
