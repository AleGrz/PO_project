package agh.darwinworld.control;

import agh.darwinworld.model.UserFriendlyException;
import javafx.scene.control.TextField;

public class IntField extends TextField {
    public IntField() {
        super();
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text) {
        if (text.isEmpty()) {
            return true;
        }
        return text.matches("-?\\d*");
    }

    public Integer getValue() {
        if (getText().isEmpty()) return null;
        try {
            return Integer.parseInt(getText());
        } catch (NumberFormatException e) {
            throw new UserFriendlyException("Invalid value!", "Value must be an integer.");
        }
    }

    public void setValue(int value) {
        setText(String.valueOf(value));
    }
}
