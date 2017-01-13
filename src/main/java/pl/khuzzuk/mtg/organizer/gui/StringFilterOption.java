package pl.khuzzuk.mtg.organizer.gui;

import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import pl.khuzzuk.mtg.organizer.dm.Card;

import java.util.function.Predicate;

class StringFilterOption implements FilterOption<Card> {
    private Predicate<Card> predicate;
    private String name;
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

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void setView(ListCell<FilterOption<Card>> cell) {
        cell.setText(name);
    }

    @Override
    public Button getButton() {
        if (button == null) {
            button = new Button(name);
        }
        return button;
    }
}
