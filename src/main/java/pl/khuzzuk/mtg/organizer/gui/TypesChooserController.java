package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.Type;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class TypesChooserController extends ChooserController<Type> {
    public TypesChooserController(Bus bus, Properties messages) {
        super(bus, messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bus.setGuiReaction(messages.getProperty("cards.receive.types"), this::load);
        items.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        endMessage = messages.getProperty("cards.receive.types.chosen");
    }

    @Override
    @FXML
    void choose() {
        bus.send(messages.getProperty("cards.receive.types.chosen"), items.getSelectionModel().getSelectedItems());
        parent.hide();
    }
}
