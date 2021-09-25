package ru.itis.parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser implements Parser{
    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>|&.+?;");
    @Override
    public String parse(String line) {
        if (line == null || line.isEmpty()) {
            return line;
        }

        Matcher m = REMOVE_TAGS.matcher(line);
        return m.replaceAll(" ");
    }
}
