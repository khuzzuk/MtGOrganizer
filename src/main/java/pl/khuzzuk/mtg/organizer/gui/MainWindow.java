package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import pl.khuzzuk.messaging.Bus;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    private Bus bus;
    @FXML
    private SplitPane editions;
    private Properties messages;

    public MainWindow(Bus bus, Properties messages) {
        this.bus = bus;
        this.messages = messages;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
