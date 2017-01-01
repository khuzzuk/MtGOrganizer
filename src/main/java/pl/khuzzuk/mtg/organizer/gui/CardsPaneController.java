package pl.khuzzuk.mtg.organizer.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.FileNameManager;
import pl.khuzzuk.mtg.organizer.dm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CardsPaneController extends ListedController<Card> {
    @FXML
    private ComboBox<CardRarity> rarity;
    @FXML
    private TextField signature;
    @FXML
    private Button editionButton;
    private Edition currentEdition;
    @FXML
    private TableView<Type> types;
    @FXML
    private ComboBox<PrimaryType> primaryType;
    @FXML
    @Numeric
    TextField blueNumber;
    @FXML
    @Numeric
    TextField greenNumber;
    @FXML
    @Numeric
    TextField redNumber;
    @FXML
    @Numeric
    TextField blackNumber;
    @FXML
    @Numeric
    TextField colorlessNumber;
    @FXML
    @Numeric
    TextField whiteNumber;
    @FXML
    private ImageView cardView;
    private File picFile;
    private int currentPicId;

    public CardsPaneController(Bus bus, Properties messages) {
        super(bus, messages);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        bus.setGuiReaction(messages.getProperty("cards.receive.all"), this::loadAll);
        bus.send(messages.getProperty("cards.load.all"), messages.getProperty("cards.receive.all"));
        bus.setGuiReaction(messages.getProperty("pics.manager.save.confirm"), this::showResults);
        bus.setGuiReaction(messages.getProperty("cards.receive.types.chosen"), this::loadTypes);
        bus.setGuiReaction(messages.getProperty("cards.receive.editions.chosen"), this::loadEdition);
        cardView.setFitWidth(500);
        cardView.setFitHeight(700);
        ComboBoxHandler.fill(PrimaryType.SET, primaryType);
        ComboBoxHandler.fill(CardRarity.SET, rarity);

        ((TableColumn<Type, String>) types.getColumns().get(0))
                .setCellValueFactory(param -> new SimpleStringProperty(
                        param.getValue().getName()));
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
        card.setWhite(Byte.parseByte(whiteNumber.getText()));
        card.setBlue(Byte.parseByte(blueNumber.getText()));
        card.setGreen(Byte.parseByte(greenNumber.getText()));
        card.setRed(Byte.parseByte(redNumber.getText()));
        card.setBlack(Byte.parseByte(blackNumber.getText()));
        card.setColorless(Byte.parseByte(colorlessNumber.getText()));
        card.setPrimaryType(ComboBoxHandler.getOrNull(primaryType));
        card.setTypes(types.getItems().stream().collect(Collectors.toSet()));
        card.setEdition(currentEdition);
        card.setRarity(ComboBoxHandler.getOrNull(rarity));
        card.setSignature(signature.getText());
        return card;
    }

    @Override
    void deleteElement(Card card) {
        bus.send(messages.getProperty("cards.delete.named"), messages.getProperty("cards.receive.all"), card);
    }

    @Override
    void load(Card card) {
        clear();
        super.load(card);
        whiteNumber.setText(card.getWhite() + "");
        blueNumber.setText(card.getBlue() + "");
        greenNumber.setText(card.getGreen() + "");
        redNumber.setText(card.getRed() + "");
        blackNumber.setText(card.getBlack() + "");
        colorlessNumber.setText(card.getColorless() + "");
        ComboBoxHandler.selectOrEmpty(primaryType, card.getPrimaryType());
        types.getItems().addAll(card.getTypes());
        editionButton.setText(card.getEdition() != null ? card.getEdition().getName() : "brak");
        currentEdition = card.getEdition();
        ComboBoxHandler.selectOrEmpty(rarity, card.getRarity());
        signature.setText(card.getSignature());
        if (card.getPicId() > 0) {
            currentPicId = card.getPicId();
            try {
                loadImage(new File(FileNameManager.getFileName(card.getPicId())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showError("Błąd wczytywania pliku", e.getMessage());
            }
        }
    }

    @Override
    void clear() {
        super.clear();
        currentPicId = 0;
        cardView.setImage(null);
        whiteNumber.clear();
        blackNumber.clear();
        blueNumber.clear();
        redNumber.clear();
        greenNumber.clear();
        colorlessNumber.clear();
        primaryType.getSelectionModel().clearSelection();
        types.getItems().clear();
        editionButton.setText("brak");
        currentEdition = null;
        rarity.getSelectionModel().clearSelection();
        signature.clear();
    }

    private void loadTypes(Collection<Type> items) {
        types.getItems().clear();
        types.getItems().addAll(items);
    }

    private void loadEdition(Edition edition) {
        editionButton.setText(edition.getName());
        currentEdition = edition;
    }

    @FXML
    void addImage() throws FileNotFoundException {
        if (currentPicId > 0) {
            bus.send(messages.getProperty("pics.manager.remove.numbered"), currentPicId);
            currentPicId = 0;
        }
        FileChooser chooser = new FileChooser();
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("*.jpg", "*.jpg", "*.jpeg"));
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
            loadImage(picFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showError("Błąd wczytywania pliku", e.getMessage());
            bus.send(messages.getProperty("pics.manager.remove.last"));
        }
    }

    private void loadImage(File file) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(file));
        cardView.setImage(image);
    }

    @FXML
    private void selectTypes() {
        bus.send(messages.getProperty("types.load.all"), messages.getProperty("cards.receive.types"));
    }

    @FXML
    private void selectEdition() {
        bus.send(messages.getProperty("editions.load.all"), messages.getProperty("cards.receive.editions"));
    }
}
