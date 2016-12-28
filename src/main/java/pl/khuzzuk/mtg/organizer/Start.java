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
    private Properties messages;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        messages = new Properties();
        messages.load(new BufferedReader(new InputStreamReader(Start.class.getResourceAsStream("/messages.properties"))));
        bus = Bus.initializeBus();
        initDao();
        ControllersFactoryFacade facade = new ControllersFactoryFacade(bus, dao, messages);
        facade.init();
        new MainWindowStage(facade, primaryStage, bus, dao).show();
    }

    private void initDao() {
        dao = new DAO();
        dao.initResolvers(Edition.class);
        bus.setReaction(messages.getProperty("editions.load.all"),
                () -> bus.send(messages.getProperty("editions.receive.all"), dao.getAllEntities(Edition.class)));
        bus.setResponseResolver(messages.getProperty("editions.save.named"), e -> {
            dao.saveEntity((Edition) e);
            return dao.getAllEntities(Edition.class);
        });
    }
}
