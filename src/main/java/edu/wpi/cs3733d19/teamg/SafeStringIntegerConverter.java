package edu.wpi.cs3733d19.teamg;

import javafx.util.StringConverter;

public class SafeStringIntegerConverter extends StringConverter<Integer> {

    /**
     * Returns an Integer that from a string that removes all non number characters.
     *
     * @param string the string to be converted
     * @return an Integer from  the string
     */
    public Integer fromString(String string) {
        string = string.replaceAll("[\\D.]", "");
        if (string.equals("")) {
            return 0;
        }
        return Integer.parseInt(string);
    }

    public String toString(Integer integer) {
        return integer.toString();
    }
}
