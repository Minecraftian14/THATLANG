package in.mcxiv.utils;

import in.mcxiv.parser.ParsableString;

public class Cursors {

    public static boolean isValueTokenCharacter(ParsableString string) {
        char c = getChar(string);
        return Character.isLetterOrDigit(c) || c == '.';
    }

    public static boolean isSpace(ParsableString string) {
        char c = getChar(string);
        return c == ' ' || c == '\t';
    }

    public static boolean isNewLine(ParsableString string) {
        char c = getChar(string);
        return c == '\n' || c == '\r';
    }

    public static boolean isBlank(ParsableString string) {
        return isSpace(string) || isNewLine(string);
    }

    public static char getChar(ParsableString string) {
        return string.charAt(string.getCursor());
    }

    public static char getCharAndNext(ParsableString string) {
        char c = string.charAt(string.getCursor());
        string.moveCursor(1);
        return c;
    }

    public static boolean bound(ParsableString string) {
        return string.getCursor() < string.length();
    }

    public static boolean matches(ParsableString string, String match) {
        return matches(string, match.toCharArray());
    }

    public static boolean matches(ParsableString string, char[] match) {
        return Strings.matches(string.getChars(), string.getCursor(), match);
    }

    public static void skipSpaces(ParsableString string) {
        while (Cursors.bound(string) && Cursors.isSpace(string)) string.moveCursor(1);
    }

    public static String fetchEverythingUpTo(ParsableString string, String symbol) {
        int start = string.getCursor();
        char[] chars = symbol.toCharArray();
        while (bound(string))
            if (matches(string, chars))
                return new String(string.getChars(), start, string.getCursor() - start);
            else string.moveCursor(1);
        return null;
    }

    /**
     * Returns the (spaces) string which lyes between the current char and the last new line.
     * <p>
     * YES! It requires the current character to be a non-space and the preceding char to be a (black).
     */
    public static String getElevation(ParsableString string) {
        int end = string.getCursor();
        String result = null;
        while (string.getCursor() > 0) {
            string.moveCursor(-1);
            int start = string.getCursor();
            if (isNewLine(string)) {
                result = (String) string.subSequence(start + 1, end - start - 1);
                break;
            }
            if (!isSpace(string)) break;
        }
        string.setCursor(end);
        return result;
    }
}
