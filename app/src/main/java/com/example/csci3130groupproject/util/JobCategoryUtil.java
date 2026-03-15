package com.example.csci3130groupproject.util;

import java.util.Locale;

public class JobCategoryUtil {

    public static String normalizeKey(String category) {
        if (category == null) {
            return "";
        }

        return category.trim().replaceAll("\\s+", " ").toLowerCase(Locale.CANADA);
    }

    public static String toDisplayName(String category) {
        String normalized = normalizeKey(category);
        if (normalized.isEmpty()) {
            return "";
        }

        String[] words = normalized.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.isEmpty()) {
                continue;
            }

            builder.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                builder.append(word.substring(1));
            }

            if (i < words.length - 1) {
                builder.append(" ");
            }
        }

        return builder.toString();
    }

    public static boolean matchesFilter(String jobCategory, String filterText) {
        String normalizedFilter = normalizeKey(filterText);
        if (normalizedFilter.isEmpty()) {
            return true;
        }
        return normalizeKey(jobCategory).equals(normalizedFilter);
    }

    public static String resolveStoredOrNewDisplayName(String storedCategory, String typedCategory) {
        String normalizedStored = normalizeKey(storedCategory);
        if (!normalizedStored.isEmpty()) {
            return storedCategory.trim();
        }
        return toDisplayName(typedCategory);
    }
}