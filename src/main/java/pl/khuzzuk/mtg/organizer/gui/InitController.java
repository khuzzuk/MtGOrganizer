package pl.khuzzuk.mtg.organizer.gui;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public interface InitController extends Initializable {
    @Override
    default void initialize(URL location, ResourceBundle resources) {
        Arrays.stream(this.getClass().getDeclaredFields()).forEach(f -> determineNumericAction(f, this));
    }

    static void determineNumericAction(Field f, InitController controller) {
        try {
            if ((f.getType().equals(TextField.class))) {
                if (f.isAnnotationPresent(Numeric.class)) {
                    setIntegerListenerToTextField((TextField) f.get(controller));
                } else if (f.isAnnotationPresent(FloatNumeric.class)) {
                    setFloatListenerToTextField((TextField) f.get(controller));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    static void setIntegerListenerToTextField(TextField field) {
        field.textProperty().addListener((obs, oldV, newV) -> field.setText(newV.replaceAll("[^\\d\\-]", "")));
    }

    static void setFloatListenerToTextField(TextField field) {
        field.textProperty().addListener((observable, oldValue, newValue) ->
                field.setText(Pattern.matches("[+-]([0-9]*)\\.([0-9]*)", newValue) ?
                        newValue : oldValue));
    }
}
