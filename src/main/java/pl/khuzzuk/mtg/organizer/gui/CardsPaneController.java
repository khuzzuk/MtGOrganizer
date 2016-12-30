package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.dm.Card;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;

public class CardsPaneController extends ListedController<Card> {
    @FXML
    private ImageView cardView;
    private File picFile;
    private int currentPicId;

    public CardsPaneController(Bus bus, Properties messages) {
        super(bus, messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        bus.setGuiReaction(messages.getProperty("cards.receive.all"), o -> loadAll((Collection<Card>) o));
        bus.send(messages.getProperty("cards.load.all"), messages.getProperty("cards.receive.all"));
        bus.setGuiReaction(messages.getProperty("pics.manager.save.confirm"), i -> showResults((Integer) i));
        cardView.setFitWidth(500);
        cardView.setFitHeight(700);
    }

    @Override
    void saveElement(Card card) {
        bus.send(messages.getProperty("cards.save.named"), messages.getProperty("cards.receive.all"), card);
    }

    @Override
    Card createElement() {
        Card card = new Card();
        card.setName(name.getText());
        card.setPicId(currentPicId);
        return card;
    }

    @Override
    void deleteElement(Card card) {
        bus.send(messages.getProperty("cards.delete.named"), messages.getProperty("cards.receive.all"), card);
    }

    @Override
    void load(Card card) {
        super.load(card);
    }

    @Override
    void clear() {
        super.clear();
        currentPicId = 0;
    }

    @FXML
    void addImage() throws FileNotFoundException {
        if (currentPicId > 0) {
            bus.send(messages.getProperty("pics.manager.remove.last"));
        }
        FileChooser chooser = new FileChooser();
        picFile = chooser.showOpenDialog(null);
        if (picFile != null && picFile.exists()) {
            bus.send(messages.getProperty("pics.manager.save"), messages.getProperty("pics.manager.save.confirm"), picFile);
        }
    }

    private void showResults(Integer value) {
        if (value < 1) {
            showError("Błąd zapisu", "plik nie został skopiowany");
            bus.send(messages.getProperty("pics.manager.remove.last"));
            return;
        }
        currentPicId = value;
        try {
            Image image = new Image(new FileInputStream(picFile));
            cardView.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showError("Błąd wczytywania pliku", e.getMessage());
            bus.send(messages.getProperty("pics.manager.remove.last"));
        }
    }
}
