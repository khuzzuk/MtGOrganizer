package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.Edition;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class EditionsPaneController extends ListedController<Edition> {
    @FXML
    @Numeric
    TextField year;

    public EditionsPaneController(Bus bus, Properties messages) {
        super(bus, messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        bus.setGuiReaction(messages.getProperty("editions.receive.all"), this::loadAll);
        bus.send(messages.getProperty("editions.load.all"), messages.getProperty("editions.receive.all"));
    }

    @Override
    @FXML
    void clear() {
        super.clear();
        year.textProperty().unbind();
        year.clear();
    }

    @Override
    void load(Edition edition) {
        clear();
        super.load(edition);
        year.setText(edition.getYear() + "");
    }

    @Override
    void saveElement(Edition edition) {
        bus.send(messages.getProperty("editions.save.named"), messages.getProperty("editions.receive.all"), edition);
    }

    @Override
    Edition createElement() {
        Edition edition = new Edition();
        edition.setName(name.getText());
        edition.setYear(Integer.parseInt(year.getText()));
        return edition;
    }

    @Override
    void deleteElement(Edition edition) {
        bus.send(messages.getProperty("editions.delete.named"), messages.getProperty("editions.receive.all"), edition);
    }
}
