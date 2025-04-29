package org.KreativeName.calculator;

import java.util.List;
import java.util.stream.Collectors;

public class CommandHelper {
    public static List<String> FilterCompletions(List<String> completions, String input) {
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static boolean IsInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
