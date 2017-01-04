package pl.khuzzuk.mtg.organizer;

import pl.khuzzuk.messaging.Bus;

import java.io.*;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PropertiesManager {
    private static final Logger log = LogManager.getLogManager().getLogger("generalLogger");
    private Bus bus;
    private Properties properties;
    private Properties messages;
    private static File propertiesFile;

    static PropertiesManager buildManager(Bus bus, Properties messages) {
        PropertiesManager manager = new PropertiesManager();
        manager.bus = bus;
        manager.properties = getProperties();
        manager.messages = messages;
        manager.setBusOperations();
        return manager;
    }

    private void setBusOperations() {
        bus.setResponseResolver(messages.getProperty("properties.get.int"), this::getIntProperty);
        bus.setResponseResolver(messages.getProperty("properties.get.boolean"), this::getBooleanProperty);
        bus.setResponseResolver(messages.getProperty("properties.get.double"), this::getDoubleProperty);
        bus.setResponseResolver(messages.getProperty("properties.get.string"), this::getStringProperty);

        bus.setReaction(messages.getProperty("properties.window.posx.set"), s -> saveProperty("posX", s));
        bus.setReaction(messages.getProperty("properties.window.posy.set"), s -> saveProperty("posY", s));
        bus.setReaction(messages.getProperty("properties.window.width.set"), s -> saveProperty("width", s));
        bus.setReaction(messages.getProperty("properties.window.height.set"), s -> saveProperty("height", s));
        bus.setReaction(messages.getProperty("properties.window.maximized.set"), s -> saveProperty("maximized", s));
        bus.setReaction(messages.getProperty("properties.files.chooser.location.set"), s -> saveProperty("lastLocation", s));
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.putIfAbsent("maximized", "false");
        properties.putIfAbsent("posX", "0");
        properties.putIfAbsent("posY", "0");
        properties.putIfAbsent("width", "900");
        properties.putIfAbsent("height", "700");

        propertiesFile = new File("app.properties");
        if (!propertiesFile.exists()) {
            return properties;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(propertiesFile))) {
            properties.load(reader);
        } catch (IOException e) {
            log.severe("no access to file: " + propertiesFile);
            log.severe(e.getMessage());
            e.printStackTrace();
        }
        return properties;
    }

    private String getStringProperty(String name) {
        return properties.getProperty(name);
    }

    private int getIntProperty(String name) {
        String property = properties.getProperty(name);
        if (property.length() == property.replaceAll("^[0-9]", "").length()) {
            return Integer.parseInt(property);
        }
        return 0;
    }

    private double getDoubleProperty(String name) {
        String property = properties.getProperty(name);
        if (property.length() == property.replaceAll("^[0-9]", "").length()) {
            return Double.parseDouble(property);
        }
        return 0;
    }

    private boolean getBooleanProperty(String name) {
        return properties.getProperty(name) != null &&
                Boolean.parseBoolean(properties.getProperty(name));
    }

    private void saveProperty(String name, Object value) {
        properties.put(name, value.toString());
        storeProperties();
    }

    private void storeProperties() {
        try (FileWriter writer = new FileWriter(propertiesFile)) {
            properties.store(writer, "");
        } catch (IOException e) {
            log.warning("Properties changes wasn't saved");
            e.printStackTrace();
        }
    }
}
