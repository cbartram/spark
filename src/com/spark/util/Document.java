package com.spark.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Document
 *
 * @author Ian
 * @version 1.0
 */
public class Document {
    private final String text;

    public Document(String text) {
        if (text == null)
            throw new IllegalArgumentException();
        this.text = text;
    }

    public String[] find(String regex) {
        return regex == null ? new String[0] : find(Pattern.compile(regex));
    }

    public String[] find(Pattern pattern) {
        if (pattern == null)
            return new String[0];
        Matcher matcher = match(pattern);
        if (!matcher.find())
            return new String[0];
        String[] group = new String[matcher.groupCount()];
        for (int i = 0; i < group.length; i++)
            group[i] = matcher.group(i);
        return group;
    }

    public String[][] findAll(String regex) {
        return regex == null ? new String[0][] : findAll(Pattern.compile(regex));
    }

    public String[][] findAll(Pattern pattern) {
        if (pattern == null)
            return new String[0][];
        Matcher matcher = match(pattern);
        List<String[]> matches = new ArrayList<>();
        while (matcher.find()) {
            String[] group = new String[matcher.groupCount()];
            for (int i = 0; i < group.length; i++)
                group[i] = matcher.group(i);
            matches.add(group);
        }
        return matches.toArray(new String[matches.size()][]);
    }

    public Matcher match(String regex) {
        return regex == null ? null : match(Pattern.compile(regex));
    }

    public Matcher match(Pattern pattern) {
        return pattern == null ? null : pattern.matcher(text);
    }

    public String getText() {
        return text;
    }
}
