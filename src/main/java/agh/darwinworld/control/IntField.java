package agh.darwinworld.control;

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

    /**
     * Returns the current value as an integer. Defaults to 0 if the field is empty.
     */
    public Integer getValue() {
        if (getText().isEmpty())
            return null;
        return Integer.parseInt(getText());
    }

    /**
     * Sets the value of the field as an integer.
     */
    public void setValue(int value) {
        setText(String.valueOf(value));
    }
}
