package com.richdyang.fundus.base.text;


import static com.richdyang.fundus.base.text.CharMatcher.*;
import static java.lang.System.out;

/**
 * Utility class for converting between various ASCII case formats.
 *
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>ince fundus
 */
public enum NameConvention {

    UNCLASSIFIED(ANY, ""),

    /**
     * Hyphenated variable naming convention, e.g., "lower-hyphen".
     */
    LOWER_HYPHEN(is('-'), "-"),

    /**
     * C++ variable naming convention, e.g., "lower_underscore".
     */
    LOWER_UNDERSCORE(is('_'), "_"),

    /**
     * Java variable naming convention, e.g., "lowerCamel".
     */
    LOWER_CAMEL(inRange('A', 'Z'), ""),

    /**
     * Java and C++ class naming convention, e.g., "UpperCamel".
     */
    UPPER_CAMEL(inRange('A', 'Z'), ""),

    /**
     * Java and C++ constant naming convention, e.g., "UPPER_UNDERSCORE".
     */
    UPPER_UNDERSCORE(is('_'), "_");

    private final CharMatcher wordBoundary;
    private final String wordSeparator;

    NameConvention(CharMatcher wordBoundary, String wordSeparator) {
        this.wordBoundary = wordBoundary;
        this.wordSeparator = wordSeparator;
    }

    /**
     * Converts the specified {@code String s} from this format to the specified {@code format}. A
     * "best effort" approach is taken; if {@code s} does not conform to the assumed format, then the
     * behavior of this method is undefined but we make a reasonable effort at converting anyway.
     */
    public String to(NameConvention format, String s) {
        if (format == null) {
            throw new NullPointerException();
        }
        if (s == null) {
            throw new NullPointerException();
        }

        if (format == this) {
            return s;
        }

    /* optimize cases where no camel conversion is required */
        switch (this) {
            case LOWER_HYPHEN:
                switch (format) {
                    case LOWER_UNDERSCORE:
                        return s.replace('-', '_');
                    case UPPER_UNDERSCORE:
                        return toUpperCaseAscii(s.replace('-', '_'));
                }
                break;
            case LOWER_UNDERSCORE:
                switch (format) {
                    case LOWER_HYPHEN:
                        return s.replace('_', '-');
                    case UPPER_UNDERSCORE:
                        return toUpperCaseAscii(s);
                }
                break;
            case UPPER_UNDERSCORE:
                switch (format) {
                    case LOWER_HYPHEN:
                        return toLowerCaseAscii(s.replace('_', '-'));
                    case LOWER_UNDERSCORE:
                        return toLowerCaseAscii(s);
                }
                break;
        }

        // otherwise, deal with camel conversion
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        while ((j = wordBoundary.indexIn(s, ++j)) != -1) {
            if (i == 0) {
                // include some extra space for separators
                out = new StringBuilder(s.length() + 4 * wordSeparator.length());
                out.append(format.normalizeFirstWord(s.substring(i, j)));
            } else {
                out.append(format.normalizeWord(s.substring(i, j)));
            }
            out.append(format.wordSeparator);
            i = j + wordSeparator.length();
        }
        if (i == 0) {
            return format.normalizeFirstWord(s);
        }
        out.append(format.normalizeWord(s.substring(i)));
        return out.toString();
    }

    private String normalizeFirstWord(String word) {
        switch (this) {
            case LOWER_CAMEL:
                return toLowerCaseAscii(word);
            default:
                return normalizeWord(word);
        }
    }

    private String normalizeWord(String word) {
        switch (this) {
            case LOWER_HYPHEN:
                return toLowerCaseAscii(word);
            case LOWER_UNDERSCORE:
                return toLowerCaseAscii(word);
            case LOWER_CAMEL:
                return firstCharOnlyToUpper(word);
            case UPPER_CAMEL:
                return firstCharOnlyToUpper(word);
            case UPPER_UNDERSCORE:
                return toUpperCaseAscii(word);
        }
        throw new RuntimeException("unknown case: " + this);
    }

    private static String firstCharOnlyToUpper(String word) {
        int length = word.length();
        if (length == 0) {
            return word;
        }
        return new StringBuilder(length)
                .append(charToUpperCaseAscii(word.charAt(0)))
                .append(toLowerCaseAscii(word.substring(1)))
                .toString();
    }

    static String toUpperCaseAscii(String string) {
        int length = string.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(charToUpperCaseAscii(string.charAt(i)));
        }
        return builder.toString();
    }

    static String toLowerCaseAscii(String string) {
        int length = string.length();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(charToLowerCaseAscii(string.charAt(i)));
        }
        return builder.toString();
    }

    private static char charToUpperCaseAscii(char c) {
        return isLowerCase(c) ? (char) (c & 0x5f) : c;
    }

    private static char charToLowerCaseAscii(char c) {
        return isUpperCase(c) ? (char) (c ^ 0x20) : c;
    }

    private static boolean isLowerCase(char c) {
        return (c >= 'a') && (c <= 'z');
    }

    private static boolean isUpperCase(char c) {
        return (c >= 'A') && (c <= 'Z');
    }

    public static void main(String[] args) {

        out.println(LOWER_CAMEL.to(UPPER_UNDERSCORE, "first"));
    }
}
