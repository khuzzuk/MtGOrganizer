package pl.khuzzuk.mtg.organizer.gui;

import javafx.scene.control.ComboBox;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.PrimaryType;
import pl.khuzzuk.mtg.organizer.dm.Type;

import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;

public class TypesPaneController extends ListedController<Type> {

    public ComboBox<PrimaryType> primaryType;

    public TypesPaneController(Bus bus, Properties messages) {
        super(bus, messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        bus.setGuiReaction(messages.getProperty("types.receive.all"), o -> loadAll((Collection<Type>) o));
        bus.send(messages.getProperty("types.load.all"), messages.getProperty("types.receive.all"));
        ComboBoxHandler.fill(PrimaryType.SET, primaryType);
    }

    @Override
    void saveElement(Type type) {
        bus.send(messages.getProperty("types.save.named"), messages.getProperty("types.receive.all"), type);
    }

    @Override
    Type createElement() {
        Type type = new Type();
        type.setName(name.getText());
        type.setPrimaryType(ComboBoxHandler.getOrNull(primaryType));
        return type;
    }

    @Override
    void deleteElement(Type type) {
        bus.send(messages.getProperty("types.delete.named"), messages.getProperty("types.receive.all"), type);
    }

    @Override
    void load(Type type) {
        super.load(type);
        ComboBoxHandler.selectOrEmpty(primaryType, type.getPrimaryType());
    }

    @Override
    void clear() {
        super.clear();
        primaryType.getSelectionModel().clearSelection();
    }
}
