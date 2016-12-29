package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.Initializable;
import javafx.util.Callback;
import pl.khuzzuk.dao.DAO;
import pl.khuzzuk.messaging.Bus;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ControllersFactoryFacade implements Callback<Class<?>, Object> {
    private Bus bus;
    private DAO dao;
    private Map<Class<?>, Initializable> factories;
    private Properties messages;

    public ControllersFactoryFacade(Bus bus, DAO dao, Properties messages) {
        this.bus = bus;
        this.dao = dao;
        this.messages = messages;
    }

    public void init() {
        factories = new HashMap<>();
        factories.put(MainWindow.class, new MainWindow(bus, messages));
        ListedController<?> controller = new EditionsPaneController(bus, messages);
        factories.put(EditionsPaneController.class, controller);
        controller = new TypesPaneController(bus, messages);
        factories.put(TypesPaneController.class, controller);
        controller = new CardsPaneController(bus, messages);
        factories.put(CardsPaneController.class, controller);
    }

    @Override
    public Object call(Class<?> param) {
        return factories.get(param);
    }
}
