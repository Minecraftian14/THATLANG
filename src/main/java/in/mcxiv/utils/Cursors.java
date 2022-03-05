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

    public static boolean isWhite(ParsableString string) {
        char c = getChar(string);
        return isSpace(string) || c == '\n' || c == '\r';
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
}
