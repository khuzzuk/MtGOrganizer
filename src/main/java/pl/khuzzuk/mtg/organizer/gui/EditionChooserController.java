package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXML;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.Edition;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class EditionChooserController extends ChooserController<Edition> {
    public EditionChooserController(Bus bus, Properties messages) {
        super(bus, messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bus.setGuiReaction(messages.getProperty("cards.receive.editions"), this::load);
        endMessage = messages.getProperty("cards.receive.editions.chosen");
    }

    @Override
    @FXML
    void choose() {
        bus.send(endMessage, items.getSelectionModel().getSelectedItem());
        parent.hide();
    }
}
