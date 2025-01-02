package agh.darwinworld.controls;

import javafx.scene.control.TextField;

/**
 * A custom text field that only accepts integer values.
 * It allows negative numbers and empty values, and provides utility methods
 * to get and set its value as an integer.
 */
public class IntField extends TextField {
    /**
     * Constructs an empty IntField.
     */
    public IntField() {
        super();
    }

    /**
     * Replaces a portion of the text with the given input, if it is valid.
     * Only integer values or an empty string are allowed.
     *
     * @param start the starting position of the range to replace.
     * @param end   the ending position of the range to replace.
     * @param text  the text to replace the range with.
     */
    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    /**
     * Replaces the current selection with the given input, if it is valid.
     * Only integer values or an empty string are allowed.
     *
     * @param text the text to replace the current selection with.
     */
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
     * Gets the integer value currently in the field.
     *
     * @return The integer value, or {@code null} if the field is empty.
     * @throws NumberFormatException if the text cannot be parsed into an integer,
     *                               such as when the value is too large or too long.
     */
    public Integer getValue() {
        if (getText().isEmpty()) return null;
        return Integer.parseInt(getText());
    }

    /**
     * Sets the value of the field to the given integer.
     *
     * @param value the integer value to set.
     */
    public void setValue(int value) {
        setText(String.valueOf(value));
    }
}
