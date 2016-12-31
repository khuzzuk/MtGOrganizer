package pl.khuzzuk.mtg.organizer;

import javafx.application.Application;
import javafx.stage.Stage;
import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.Card;
import pl.khuzzuk.mtg.organizer.dm.Edition;
import pl.khuzzuk.mtg.organizer.dm.Type;
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
        initPicManager();
        new MainWindowStage(facade, primaryStage, bus, dao).show();
    }

    private void initDao() {
        dao = new DAO();
        dao.initResolvers(Edition.class, Type.class, Card.class);
        bus.setReaction(messages.getProperty("editions.load.all"),
                () -> bus.send(messages.getProperty("editions.receive.all"), dao.getAllEntities(Edition.class)));
        bus.setResponseResolver(messages.getProperty("editions.save.named"), e -> {
            dao.saveEntity((Edition) e);
            return dao.getAllEntities(Edition.class);
        });
        bus.setResponseResolver(messages.getProperty("editions.delete.named"), e -> {
            dao.removeEntity((Edition) e);
            return dao.getAllEntities(Edition.class);
        });

        bus.setResponse(messages.getProperty("types.load.all"), () -> dao.getAllEntities(Type.class));
        bus.setResponseResolver(messages.getProperty("types.save.named"), t -> {
            dao.saveEntity((Type) t);
            return dao.getAllEntities(Type.class);
        });
        bus.setResponseResolver(messages.getProperty("types.delete.named"), t -> {
            dao.removeEntity((Type) t);
            return dao.getAllEntities(Type.class);
        });

        bus.setResponse(messages.getProperty("cards.load.all"), () -> dao.getAllEntities(Card.class));
        bus.setResponseResolver(messages.getProperty("cards.save.named"), c -> {
            dao.saveEntity((Card) c);
            return dao.getAllEntities(Card.class);
        });
        bus.setResponseResolver(messages.getProperty("cards.delete.named"), c -> {
            dao.removeEntity((Card) c);
            return dao.getAllEntities(Card.class);
        });
    }

    private void initPicManager() {
        PicIdManager manager = new PicIdManager();
        bus.setReaction(messages.getProperty("pics.manager.init"), manager::init);
        bus.send(messages.getProperty("pics.manager.init"));
        bus.setResponseResolver(messages.getProperty("pics.manager.save"), manager::saveFile);
        bus.setReaction(messages.getProperty("pics.manager.remove.last"), manager::removeLast);
        bus.setReaction(messages.getProperty("pics.manager.remove.numbered"), manager::removeNumbered);
    }
}
