package pl.khuzzuk.mtg.organizer.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import pl.khuzzuk.messaging.Bus;
import pl.khuzzuk.mtg.organizer.FileNameManager;
import pl.khuzzuk.mtg.organizer.StringOptional;
import pl.khuzzuk.mtg.organizer.dm.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CardsPaneController extends ListedController<Card> {
    public HBox filters;
    private Set<FilterOption<Card>> usedFilters;
    @FXML
    private ComboBox<FilterOption<Card>> filtersSet;
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
    private String lastFileLocation;

    CardsPaneController(Bus bus, Properties messages) {
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
        bus.setReaction(messages.getProperty("properties.files.chooser.location"), this::setLastFileLocation);
        bus.send(messages.getProperty("properties.get.string"), messages.getProperty("properties.files.chooser.location"), "lastLocation");
        ComboBoxHandler.fill(PrimaryType.SET, primaryType);
        ComboBoxHandler.fill(CardRarity.SET, rarity);

        ((TableColumn<Type, String>) types.getColumns().get(0))
                .setCellValueFactory(param -> new SimpleStringProperty(
                        param.getValue().getName()));

        addFilters();
    }

    private void addFilters() {
        usedFilters = new HashSet<>(8);
        filtersSet.setCellFactory(param -> new FilterCell<>());
        filtersSet.getItems().addAll(buildIconFilter("white mana", new Image("Mana_W.png"), c -> c.getWhite() > 0),
                buildIconFilter("green mana", new Image("Mana_G.png"), c -> c.getGreen() > 0),
                buildIconFilter("blue mana", new Image("Mana_N.png"), c -> c.getBlue() > 0),
                buildIconFilter("red mana", new Image("Mana_R.png"), c -> c.getRed() > 0),
                buildIconFilter("black mana", new Image("Mana_B.png"), c -> c.getBlack() > 0),
                buildStringFilter("Mythics", c -> c.getRarity() == CardRarity.MYTHIC));
    }

    private FilterOption<Card> buildIconFilter(String name, Image representation,
                                                    Predicate<Card> predicate) {
        IconFilterOption filter = new IconFilterOption();
        filter.setName(name);
        filter.setRepresentation(representation);
        filter.setPredicate(predicate);
        filter.getButton().setOnAction(e -> {
            filters.getChildren().remove(filter.getButton());
            usedFilters.remove(filter);
            sendFilterRequest();
        });
        return filter;
    }

    private FilterOption<Card> buildStringFilter(String name, Predicate<Card> predicate) {
        StringFilterOption filter = new StringFilterOption();
        filter.setName(name);
        filter.setPredicate(predicate);
        filter.getButton().setOnAction(e -> {
            filters.getChildren().remove(filter.getButton());
            usedFilters.remove(filter);
            sendFilterRequest();
        });
        return filter;
    }

    private Collection<Predicate<Card>> getPredicates() {
        return usedFilters.stream().map(FilterOption::getPredicate).collect(Collectors.toList());
    }

    private void addFilter(FilterOption<Card> filter) {
        usedFilters.add(filter);
        filters.getChildren().add(filter.getButton());
    }

    @FXML
    private void selectFilter() {
        Optional.ofNullable(ComboBoxHandler.getOrNull(filtersSet)).ifPresent(this::addFilter);
        sendFilterRequest();
    }

    private void sendFilterRequest() {
        bus.send(messages.getProperty("cards.load.filtered"), messages.getProperty("cards.receive.all"), getPredicates());
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
        card.setWhite(Byte.parseByte(new StringOptional(whiteNumber.getText()).orElseGet("0")));
        card.setBlue(Byte.parseByte(new StringOptional(blueNumber.getText()).orElseGet("0")));
        card.setGreen(Byte.parseByte(new StringOptional(greenNumber.getText()).orElseGet("0")));
        card.setRed(Byte.parseByte(new StringOptional(redNumber.getText()).orElseGet("0")));
        card.setBlack(Byte.parseByte(new StringOptional(blackNumber.getText()).orElseGet("0")));
        card.setColorless(Byte.parseByte(new StringOptional(colorlessNumber.getText()).orElseGet("0")));
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
        if (lastFileLocation != null) {
            chooser.setInitialDirectory(new File(lastFileLocation));
        }
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("*.jpg", "*.jpg", "*.jpeg"));
        picFile = chooser.showOpenDialog(null);
        if (picFile != null && picFile.exists()) {
            bus.send(messages.getProperty("pics.manager.save"), messages.getProperty("pics.manager.save.confirm"), picFile);
            lastFileLocation = picFile.getParent();
            bus.send(messages.getProperty("properties.files.chooser.location.set"), (Object) lastFileLocation);
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
        bus.send(messages.getProperty("types.load.filtered"), messages.getProperty("cards.receive.types"),
                ComboBoxHandler.getOrNull(primaryType));
    }

    @FXML
    private void selectEdition() {
        bus.send(messages.getProperty("editions.load.all"), messages.getProperty("cards.receive.editions"));
    }

    public void setLastFileLocation(String lastFileLocation) {
        this.lastFileLocation = lastFileLocation;
    }
}
