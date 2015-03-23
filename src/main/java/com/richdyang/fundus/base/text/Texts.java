/* 
 *  Copyright (c)2009-2010 The Inframesh Software Foundation (ISF)
 *
 *  Licensed under the Inframesh Software License (the "License"), 
 *	Version 1.0 ; you may obtain a copy of the license at
 *
 *  	http://www.inframesh.org/licenses/LICENSE-1.0
 *
 *  Software distributed under the License is distributed  on an "AS IS" 
 *  BASIS but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the License 
 *  for more details.
 *  
 *  Inframesh Software Foundation is donated by Drowell Technology Limited.
 */
package com.richdyang.fundus.base.text;

import static com.richdyang.fundus.base.ArgumentAssert.isTrue;

import java.util.Formatter;

/**
 * Static utility methods pertaining to {@code String} or {@code CharSequence} or {@code char}
 * instances.
 * 
 * @author <a href="mailto:richd.yang@gmail.com">Richard Yang</a>ince fundus
 */
public final class Texts {
	private Texts() {
	}

	/**
	 * Returns the given string if it is non-null; the empty string otherwise.
	 * 
	 * @param string
	 *            the string to test and possibly return
	 * @return {@code string} itself if it is non-null; {@code ""} if it is null
	 */
	public static String nullToEmpty(String string) {
		return (string == null) ? "" : string;
	}

	/**
	 * Returns the given string if it is nonempty; {@code null} otherwise.
	 * 
	 * @param string
	 *            the string to test and possibly return
	 * @return {@code string} itself if it is nonempty; {@code null} if it is
	 *         empty or null
	 */
	public static String emptyToNull(String string) {
		return isNullOrEmpty(string) ? null : string;
	}

	/**
	 * Returns {@code true} if the given string is null or is the empty string.
	 * 
	 * <p>
	 * Consider normalizing your string references with {@link #nullToEmpty}. If
	 * you do, you can use {@link String#empty()} instead of this method, and
	 * you won't need special null-safe forms of methods like
	 * {@link String#toUpperCase} either. Or, if you'd like to normalize "in the
	 * other direction," converting empty strings to {@code null}, you can use
	 * {@link #emptyToNull}.
	 * 
	 * @param string
	 *            a string reference to check
	 * @return {@code true} if the string is null or is the empty string
	 */
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0; // string.isEmpty() in
		// Java 6
	}

	/**
	 * Returns a string, of length at least {@code minLength}, consisting of
	 * {@code string} prepended with as many copies of {@code padChar} as are
	 * necessary to reach that length. For example,
	 * 
	 * <ul>
	 * <li>{@code padStart("7", 3, '0')} returns {@code "007"}
	 * <li>{@code padStart("2010", 3, '0')} returns {@code "2010"}
	 * </ul>
	 * 
	 * <p>
	 * See {@link Formatter} for a richer set of formatting capabilities.
	 * 
	 * @param string
	 *            the string which should appear at the end of the result
	 * @param minLength
	 *            the minimum length the resulting string must have. Can be zero
	 *            or negative, in which case the input string is always
	 *            returned.
	 * @param padChar
	 *            the character to insert at the beginning of the result until
	 *            the minimum length is reached
	 * @return the padded string
	 */
	public static String padStart(String string, int minLength, char padChar) {
		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		sb.append(string);
		return sb.toString();
	}

	/**
	 * Returns a string, of length at least {@code minLength}, consisting of
	 * {@code string} appended with as many copies of {@code padChar} as are
	 * necessary to reach that length. For example,
	 * 
	 * <ul>
	 * <li>{@code padEnd("4.", 5, '0')} returns {@code "4.000"}
	 * <li>{@code padEnd("2010", 3, '!')} returns {@code "2010"}
	 * </ul>
	 * 
	 * <p>
	 * See {@link Formatter} for a richer set of formatting capabilities.
	 * 
	 * @param string
	 *            the string which should appear at the beginning of the result
	 * @param minLength
	 *            the minimum length the resulting string must have. Can be zero
	 *            or negative, in which case the input string is always
	 *            returned.
	 * @param padChar
	 *            the character to append to the end of the result until the
	 *            minimum length is reached
	 * @return the padded string
	 */
	public static String padEnd(String string, int minLength, char padChar) {
		if (string.length() >= minLength) {
			return string;
		}
		StringBuilder sb = new StringBuilder(minLength);
		sb.append(string);
		for (int i = string.length(); i < minLength; i++) {
			sb.append(padChar);
		}
		return sb.toString();
	}

	/**
	 * Returns a string consisting of a specific number of concatenated copies
	 * of an input string. For example, {@code repeat("hey", 3)} returns the
	 * string {@code "heyheyhey"}.
	 * 
	 * @param string
	 *            any non-null string
	 * @param count
	 *            the number of times to repeat it; a nonnegative integer
	 * @return a string containing {@code string} repeated {@code count} times
	 *         (the empty string if {@code count} is zero)
	 * @throws IllegalArgumentException
	 *             if {@code count} is negative
	 */
	public static String repeat(String string, int count) {
		isTrue(count >= 0, "invalid count: %s", count);

		// If this multiplication overflows, a NegativeArraySizeException or
		// OutOfMemoryError is not far behind
		StringBuilder builder = new StringBuilder(string.length() * count);
		for (int i = 0; i < count; i++) {
			builder.append(string);
		}
		return builder.toString();
	}

	public static EscapeHelper needEscape() {
		return EscapeHelper.INSTANCE;
	}

	// ---------------------------------------------------------------------
	// General convenience methods for working with Strings
	// ---------------------------------------------------------------------

	/**
	 * Check that the given CharSequence is neither <code>null</code> nor of
	 * length 0. Note: Will return <code>true</code> for a CharSequence that
	 * purely consists of whitespace.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * 
	 * @param str
	 *            the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

	/**
	 * Check that the given String is neither <code>null</code> nor of length 0.
	 * Note: Will return <code>true</code> for a String that purely consists of
	 * whitespace.
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 * @see #hasLength(CharSequence)
	 */
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}

	/**
	 * Check whether the given CharSequence has actual text. More specifically,
	 * returns <code>true</code> if the string not <code>null</code>, its length
	 * is greater than 0, and it contains at least one non-whitespace character.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * 
	 * @param str
	 *            the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not <code>null</code>,
	 *         its length is greater than 0, and it does not contain whitespace
	 *         only
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String has actual text. More specifically,
	 * returns <code>true</code> if the string not <code>null</code>, its length
	 * is greater than 0, and it contains at least one non-whitespace character.
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not <code>null</code>, its
	 *         length is greater than 0, and it does not contain whitespace only
	 * @see #hasText(CharSequence)
	 */
	public static boolean hasText(String str) {
		return hasText((CharSequence) str);
	}
	
	/**
	 * Check whether the given CharSequence contains any whitespace characters.
	 * @param str the CharSequence to check (may be <code>null</code>)
	 * @return <code>true</code> if the CharSequence is not empty and
	 * contains at least 1 whitespace character
	 * @see java.lang.Character#isWhitespace
	 */
	public static boolean containsWhitespace(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String contains any whitespace characters.
	 * @param str the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not empty and
	 * contains at least 1 whitespace character
	 * @see #containsWhitespace(CharSequence)
	 */
	public static boolean containsWhitespace(String str) {
		return containsWhitespace((CharSequence) str);
	}

	/**
	 * Always make sure a non-null string returned
	 * 
	 * @param string
	 * @return a NON-NULL string
	 */
	public static String nullSafe(String string) {
		return nullSafe(string, false);
	}

	/**
	 * A non-null string returned, white space avoid
	 * 
	 * @param string
	 * @param ignoreWhitespace
	 * @return a NON-NULL string without any whitespace characters if ignoring
	 *         whitespace
	 */
	public static String nullSafe(String string, boolean ignoreWhitespace) {
		if (string == null)
			return "";

		if (ignoreWhitespace) {
			return nullSafe(string).replace("\\s", "");
		}
		return string;
	}

	public static class MutateHelper {

		/**
		 * Delete all occurrences of the given substring.
		 * 
		 * @param inString
		 *            the original String
		 * @param pattern
		 *            the pattern to delete all occurrences of
		 * @return the resulting String
		 */
		public static String delete(String inString, String pattern) {
			return replace(inString, pattern, "");
		}

		/**
		 * Delete any character in a given String.
		 * 
		 * @param inString
		 *            the original String
		 * @param charsToDelete
		 *            a set of characters to delete. E.g. "az\n" will delete
		 *            'a's, 'z's and new lines.
		 * @return the resulting String
		 */
		public static String deleteAny(String inString, String charsToDelete) {
			if (!hasLength(inString) || !hasLength(charsToDelete)) {
				return inString;
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < inString.length(); i++) {
				char c = inString.charAt(i);
				if (charsToDelete.indexOf(c) == -1) {
					sb.append(c);
				}
			}
			return sb.toString();
		}

		/**
		 * Delete any character in a given String.
		 * 
		 * @param inString
		 *            the original String
		 * @param charsToDelete
		 *            a set of characters to delete. E.g. "az\n" will delete
		 *            'a's, 'z's and new lines.
		 * @return the resulting String
		 * @see {@link TextUtil#deleteAny(String, String)}
		 */
		public static String deleteAny(String inString, char... charsToDelete) {
			return deleteAny(inString, new String(charsToDelete));
		}

		/**
		 * Trim <i>all</i> whitespace from the given String: leading, trailing,
		 * and inbetween characters.
		 * 
		 * @param str
		 *            the String to check
		 * @return the trimmed String (never be null)
		 * @see java.lang.Character#isWhitespace
		 */
		public static String trimAllWhitespace(String str) {
			if (!hasLength(str)) {
				return str;
			}
			StringBuilder sb = new StringBuilder(str);
			int index = 0;
			while (sb.length() > index) {
				if (Character.isWhitespace(sb.charAt(index))) {
					sb.deleteCharAt(index);
				} else {
					index++;
				}
			}
			return sb.toString();
		}

		/**
		 * Trim leading whitespace from the given String.
		 * 
		 * @param str
		 *            the String to check
		 * @return the trimmed String
		 * @see java.lang.Character#isWhitespace
		 */
		public static String trimLeadingWhitespace(String str) {
			if (!hasLength(str)) {
				return str;
			}
			StringBuilder sb = new StringBuilder(str);
			while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
				sb.deleteCharAt(0);
			}
			return sb.toString();
		}

		/**
		 * Trim trailing whitespace from the given String.
		 * 
		 * @param str
		 *            the String to check
		 * @return the trimmed String
		 * @see java.lang.Character#isWhitespace
		 */
		public static String trimTrailingWhitespace(String str) {
			if (!hasLength(str)) {
				return str;
			}
			StringBuilder sb = new StringBuilder(str);
			while (sb.length() > 0
					&& Character.isWhitespace(sb.charAt(sb.length() - 1))) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		}

		/**
		 * Trim all occurences of the supplied leading character from the given
		 * String.
		 * 
		 * @param str
		 *            the String to check
		 * @param leadingCharacter
		 *            the leading character to be trimmed
		 * @return the trimmed String
		 */
		public static String trimLeadingCharacter(String str,
				char leadingCharacter) {
			if (!hasLength(str)) {
				return str;
			}
			StringBuilder sb = new StringBuilder(str);
			while (sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
				sb.deleteCharAt(0);
			}
			return sb.toString();
		}

		/**
		 * Trim all occurences of the supplied trailing character from the given
		 * String.
		 * 
		 * @param str
		 *            the String to check
		 * @param trailingCharacter
		 *            the trailing character to be trimmed
		 * @return the trimmed String
		 */
		public static String trimTrailingCharacter(String str,
				char trailingCharacter) {
			if (!hasLength(str)) {
				return str;
			}
			StringBuilder sb = new StringBuilder(str);
			while (sb.length() > 0
					&& sb.charAt(sb.length() - 1) == trailingCharacter) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		}

		/**
		 * Replace all occurences of a substring within a string with another
		 * string.
		 * 
		 * @param inString
		 *            String to examine
		 * @param oldPattern
		 *            String to replace
		 * @param newPattern
		 *            String to insert
		 * @return a String with the replacements
		 */
		public static String replace(String inString, String oldPattern,
				String newPattern) {
			if (!hasLength(inString) || !hasLength(oldPattern)
					|| newPattern == null) {
				return inString;
			}
			StringBuilder sb = new StringBuilder();
			int pos = 0; // our position in the old string
			int index = inString.indexOf(oldPattern);
			// the index of an occurrence we've found, or -1
			int patLen = oldPattern.length();
			while (index >= 0) {
				sb.append(inString.substring(pos, index));
				sb.append(newPattern);
				pos = index + patLen;
				index = inString.indexOf(oldPattern, pos);
			}
			sb.append(inString.substring(pos));
			// remember to append any characters to the right of a match
			return sb.toString();
		}
	}

	public static class FormatHelper {
		// ---------------------------------------------------------------------
		// Convenience methods for working with formatted Strings
		// ---------------------------------------------------------------------

		/**
		 * Quote the given String with single quotes.
		 * 
		 * @param str
		 *            the input String (e.g. "myString")
		 * @return the quoted String (e.g. "'myString'"), or
		 *         <code>null<code> if the input was <code>null</code>
		 */
		public static String quote(String str) {
			return (str != null ? "'" + str + "'" : null);
		}

		/**
		 * Turn the given Object into a String with single quotes if it is a
		 * String; keeping the Object as-is else.
		 * 
		 * @param obj
		 *            the input Object (e.g. "myString")
		 * @return the quoted String (e.g. "'myString'"), or the input object
		 *         as-is if not a String
		 */
		public static Object quoteIfString(Object obj) {
			return (obj instanceof String ? quote((String) obj) : obj);
		}

		/**
		 * Unqualify a string qualified by a '.' dot character. For example,
		 * "this.name.is.qualified", returns "qualified".
		 * 
		 * @param qualifiedName
		 *            the qualified name
		 */
		public static String unqualify(String qualifiedName) {
			return unqualify(qualifiedName, '.');
		}

		/**
		 * Unqualify a string qualified by a separator character. For example,
		 * "this:name:is:qualified" returns "qualified" if using a ':'
		 * separator.
		 * 
		 * @param qualifiedName
		 *            the qualified name
		 * @param separator
		 *            the separator
		 */
		public static String unqualify(String qualifiedName, char separator) {
			return qualifiedName
					.substring(qualifiedName.lastIndexOf(separator) + 1);
		}

		/**
		 * Capitalize a <code>String</code>, changing the first letter to upper
		 * case as per {@link Character#toUpperCase(char)}. No other letters are
		 * changed.
		 * 
		 * @param str
		 *            the String to capitalize, may be <code>null</code>
		 * @return the capitalized String, <code>null</code> if null
		 */
		public static String capitalize(String str) {
			return changeFirstCharacterCase(str, true);
		}

		/**
		 * Uncapitalize a <code>String</code>, changing the first letter to
		 * lower case as per {@link Character#toLowerCase(char)}. No other
		 * letters are changed.
		 * 
		 * @param str
		 *            the String to uncapitalize, may be <code>null</code>
		 * @return the uncapitalized String, <code>null</code> if null
		 */
		public static String uncapitalize(String str) {
			return changeFirstCharacterCase(str, false);
		}

		private static String changeFirstCharacterCase(String str,
				boolean capitalize) {
			if (str == null || str.length() == 0) {
				return str;
			}
			StringBuilder sb = new StringBuilder(str.length());
			if (capitalize) {
				sb.append(Character.toUpperCase(str.charAt(0)));
			} else {
				sb.append(Character.toLowerCase(str.charAt(0)));
			}
			sb.append(str.substring(1));
			return sb.toString();
		}
	}

	public static class EscapeHelper {

		static EscapeHelper INSTANCE = new EscapeHelper();

		private EscapeHelper() {
		}

		/**
		 * Unimplementation
		 * 
		 * @param sqlParamter
		 * @return
		 */
		public static String escapeSQL(String sqlParamter) {
			// TODO
			return null;
		}

		/**
		 * Unimplementation
		 * 
		 * @param url
		 * @return
		 */
		public static String escapeURL(String url) {
			return null;
		}

		/**
		 * Unimplementation
		 * 
		 * @param html
		 * @return
		 */
		public static String escapeHTML(String html) {
			return null;
		}

		/**
		 * Unimplementation
		 * 
		 * @param javascript
		 * @return
		 */
		public static String escapeJavaScript(String javascript) {
			return null;
		}

		/**
		 * 转义字符串
		 * 
		 * @param str
		 *            字符串
		 * @return 返回转义后的字符串
		 */
		public static CharSequence escape(CharSequence str) {
			StringBuffer buff = new StringBuffer();
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				buff.append(escape(c));
			}
			return buff;
		}

		/**
		 * 转义字符
		 * 
		 * @param c
		 *            字符
		 * @return 返回转义后的字符串
		 */
		public static CharSequence escape(char c) {
			StringBuffer buff = new StringBuffer();
			switch (c) {
			case '/':
				buff.append("\\/");
				break;
			case '"':
				buff.append("\\\"");
				break;
			case '\\':
				buff.append("\\\\");
				break;
			case '\b':
				buff.append("\\b");
				break;
			case '\r':
				buff.append("\\r");
				break;
			case '\n':
				buff.append("\\n");
				break;
			case '\t':
				buff.append("\\t");
				break;
			case '\f':
				buff.append("\\f");
				break;
			default:
				buff.append(c);
				break;
			}
			return buff;
		}
	}
}
