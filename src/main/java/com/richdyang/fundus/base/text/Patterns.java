package com.richdyang.fundus.base.text;

/**
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>
 * @since fundus
 */
public class Patterns {

    /**
     * Converts wildcard expression to regular expression. In wildcard-format, * =
     * 0-N characters and ? = any one character. Wildcards can be used easily
     * with JDK 1.4 by converting them to regexps:
     * <p>
     * <pre><code>
     * import java.util.regex.Pattern;
     * <p>
     * Pattern p = Pattern.compile(wildcardToRegex("*.jpg"));
     * </code></pre>
     *
     * @param wildcard wildcard expression string
     * @return given wildcard expression as regular expression
     */
    public static String wildcardToRegex(String wildcard) {
        StringBuffer s = new StringBuffer(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch (c) {
                case '*':
                    s.append('.');
                    s.append('*');
                    break;
                case '?':
                    s.append('.');
                    break;
                // escape special regexp-characters
                case '(':
                case ')':
                case '[':
                case ']':
                case '$':
                case '^':
                case '.':
                case '{':
                case '}':
                case '|':
                case '\\':
                    s.append('\\');
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return (s.toString());
    }
}
