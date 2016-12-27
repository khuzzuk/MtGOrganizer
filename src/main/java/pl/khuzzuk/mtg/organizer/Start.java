package pl.khuzzuk.mtg.organizer;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.Edition;
import pl.khuzzuk.mtg.organizer.gui.ControllersFactoryFacade;
import pl.khuzzuk.mtg.organizer.gui.MainWindowStage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class Start extends Application {
    private DAO dao;
    private Bus bus;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties messages = new Properties();
        messages.load(new BufferedReader(new InputStreamReader(Start.class.getResourceAsStream("/messages.properties"))));
        dao = new DAO();
        dao.initResolvers(Edition.class);
        bus = Bus.initializeBus();
        ControllersFactoryFacade facade = new ControllersFactoryFacade(bus, dao, messages);
        facade.init();
        new MainWindowStage(facade, primaryStage, bus, dao).show();
    }
}
