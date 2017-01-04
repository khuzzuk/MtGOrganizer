package pl.khuzzuk.mtg.organizer.gui;

import javafx.application.Platform;
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
import java.util.Properties;

public class MainWindowStage extends Stage {
    private Properties messages;
    private Bus bus;
    private DAO dao;

    public MainWindowStage(ControllersFactoryFacade factoryFacade, Window parent, Bus bus, DAO dao, Properties messages) {
        super(StageStyle.DECORATED);
        this.bus = bus;
        this.dao = dao;
        this.messages = messages;
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
            Platform.exit();
            dao.close();
            bus.closeBus();
        });
        setBusReaction();
        saveWindowLocation();
    }

    private void setBusReaction() {
        bus.setGuiReaction(messages.getProperty("properties.window.posx"), this::setX);
        bus.setGuiReaction(messages.getProperty("properties.window.posy"), this::setY);
        bus.setGuiReaction(messages.getProperty("properties.window.width"), this::setWidth);
        bus.setGuiReaction(messages.getProperty("properties.window.height"), this::setHeight);
        bus.setGuiReaction(messages.getProperty("properties.window.maximized"), this::setMaximized);
        bus.send(messages.getProperty("properties.get.double"),
                messages.getProperty("properties.window.posx"), "posX");
        bus.send(messages.getProperty("properties.get.double"),
                messages.getProperty("properties.window.posy"), "posY");
        bus.send(messages.getProperty("properties.get.double"),
                messages.getProperty("properties.window.width"), "width");
        bus.send(messages.getProperty("properties.get.double"),
                messages.getProperty("properties.window.height"), "height");
        bus.send(messages.getProperty("properties.get.boolean"),
                messages.getProperty("properties.window.maximized"), "maximized");
    }

    private void saveWindowLocation() {
        widthProperty().addListener((o, v1, v2) ->
                bus.send(messages.getProperty("properties.window.width.set"), v2));
        heightProperty().addListener((o, v1, v2) ->
                bus.send(messages.getProperty("properties.window.height.set"), v2));
        xProperty().addListener((o, v1, v2) ->
                bus.send(messages.getProperty("properties.window.posx.set"), v2));
        yProperty().addListener((o, v1, v2) ->
                bus.send(messages.getProperty("properties.window.posy.set"), v2));
        maximizedProperty().addListener((o, v1, v2) ->
                bus.send(messages.getProperty("properties.window.maximized.set"), v2));
    }
}
