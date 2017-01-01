package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ChooserStage extends Stage {
    private static final Logger log = LogManager.getLogManager().getLogger("generalLogger");
    private ChooserController<?> controller;
    private String fxmlPath = "/chooserPane.fxml";

    private ChooserStage() {
        super(StageStyle.DECORATED);
    }

    public ChooserStage(ChooserController<?> controller) {
        this();
        this.controller = controller;
    }

    public void init() {
        controller.setParent(this);
        initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.setControllerFactory(c -> controller);
        try {
            setScene(new Scene(loader.load()));
        } catch (IOException e) {
            log.log(Level.SEVERE, "initialization of ChooserStage failed");
            log.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
        }
    }
}
