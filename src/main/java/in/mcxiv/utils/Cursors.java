package in.mcxiv.utils;

import in.mcxiv.thatlang.parser.ParsableString;

public class Cursors {
    public static boolean isLetterOrDigit(ParsableString string) {
        return Character.isLetterOrDigit(getChar(string));
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
}
