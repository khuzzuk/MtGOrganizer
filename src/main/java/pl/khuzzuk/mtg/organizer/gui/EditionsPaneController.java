package pl.khuzzuk.mtg.organizer.gui;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.Edition;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class EditionsPaneController extends ListedController<Edition> {
    @FXML
    @Numeric
    TextField year;

    public EditionsPaneController(Bus bus, DAO dao, Properties messages) {
        super(bus, dao, messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        item = new Edition();
    }

    void clear() {
        super.clear();
        year.textProperty().unbind();
        year.clear();
        item = new Edition();
    }

    public void load() {
        clear();
        Edition edition = items.getSelectionModel().getSelectedItem();
        if (edition != null) {
            item = edition;
            name.textProperty().bind(getNameBinding());
            year.textProperty().bind(getYearBinding());
        }
    }

    private StringBinding getYearBinding() {
        StringBinding binding = new StringBinding() {
            @Override
            protected String computeValue() {
                return item.getYear() + "";
            }
        };
        binding.addListener((o, oldValue, newValue) -> item.setYear(Integer.parseInt(newValue)));
        return binding;
    }
}
