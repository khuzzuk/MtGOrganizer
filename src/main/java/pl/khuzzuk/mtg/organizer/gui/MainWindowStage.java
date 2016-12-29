package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.messaging.Bus;

import java.io.IOException;

public class MainWindowStage extends Stage {
    private Bus bus;
    private DAO dao;
    public MainWindowStage(ControllersFactoryFacade factoryFacade, Window parent, Bus bus, DAO dao) {
        super(StageStyle.DECORATED);
        this.bus = bus;
        this.dao = dao;
        initOwner(parent);
        initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainWindow.fxml"));
        loader.setControllerFactory(factoryFacade);
        try {
            Parent root = loader.load();
            setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("can't find mainWindow.fxml");
        }
        setOnCloseRequest(e -> {
            close();
            dao.close();
            bus.closeBus();});
    }
}
