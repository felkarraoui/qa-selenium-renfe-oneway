package com.company.renfe.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility to extract and parse Euro prices from text.
 * Supports European formats like "1.250,00 €" or "59,50€".
 */
public class PriceParser {

    /**
     * Parses the first numeric value in the text as a double.
     * Removes thousand separators and converts decimal commas to dots.
     *
     * @param title Text containing a price.
     * @return Parsed price as double, or NaN if parsing fails.
     */
    public static double parseEuro(String title) {
        if (title == null) return Double.NaN;

        Matcher m = Pattern.compile("(\\d{1,3}(?:\\.\\d{3})*(?:,\\d{2})|\\d+(?:,\\d{2})?)").matcher(title);
        if (!m.find()) return Double.NaN;

        String num = m.group(1);
        String normalized = num.replace(".", "").replace(",", ".");

        try {
            return Double.parseDouble(normalized);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}
