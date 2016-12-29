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

    public CardsPaneController(Bus bus, Properties messages) {
        super(bus, messages);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        bus.setGuiReaction(messages.getProperty("cards.receive.all"), o -> loadAll((Collection<Card>) o));
        bus.send(messages.getProperty("cards.load.all"), messages.getProperty("cards.receive.all"));
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
    }

    @FXML
    void addImage() throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(null);
        if (file != null && file.exists()) {
            Image image = new Image(new FileInputStream(file));
            cardView.setImage(image);
        }
    }
}
