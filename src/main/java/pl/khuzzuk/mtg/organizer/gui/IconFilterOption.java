package pl.khuzzuk.mtg.organizer.gui;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import pl.khuzzuk.mtg.organizer.dm.Card;

import java.util.function.Predicate;

class IconFilterOption implements FilterOption<Card> {
    private Predicate<Card> predicate;
    private String name;
    private Image representation;
    private Button button;

    @Override
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    @Override
    public Predicate<Card> getPredicate() {
        return predicate;
    }

    void setPredicate(Predicate<Card> predicate) {
        this.predicate = predicate;
    }

    public Image getRepresentation() {
        return representation;
    }

    public void setRepresentation(Image representation) {
        this.representation = representation;
        if (button == null) {
            button = new Button();
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setBackground(getViewFrom(representation));
            button.setPrefWidth(30);
            button.setPrefHeight(30);
        }
    }

    private Background getViewFrom(Image image) {
        BackgroundImage back = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, new BackgroundSize(30, 30, false, false, false, false));
        return new Background(back);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void setView(ListCell<FilterOption<Card>> cell) {
        cell.setBackground(getViewFrom(representation));
    }

    @Override
    public ButtonBase getButton() {
        if (button == null) {
            button = new Button();
        }
        return button;
    }
}
