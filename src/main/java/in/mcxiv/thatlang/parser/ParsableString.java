package in.mcxiv.thatlang.parser;

import javax.xml.stream.events.Characters;
import java.util.Arrays;

public class ParsableString implements CharSequence, Comparable<ParsableString> {

    /**
     * Represents the characters consisting of the string this parsable string is.
     * Note that this sequence can actually be a lot longer than the actual chars.
     * That's why we have a separate int length to keep track of the size.
     */
    private char[] chars;

    /**
     * The actual size of this string.
     * The span from 0 to length of chars.
     */
    private int length;

    /**
     * A variable to be used by a parser to denote how much was parsed...
     * Hmmm...
     */
    private int cursor;

    public ParsableString(String string) {
        this(string.toCharArray());
    }

    public ParsableString(char[] chars) {
        this(chars, chars.length);
    }

    public ParsableString(char[] chars, int length) {
        setChars(chars, length);
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return chars[index];
    }

    @Override
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new ParsableString(Arrays.copyOfRange(chars, start, end));
    }

    @Override
    public int compareTo(ParsableString o) {
        int lim = Math.min(length, o.length);
        for (int k = 0; k < lim; k++) {
            if (chars[k] != o.chars[k]) {
                return chars[k] - o.chars[k];
            }
        }
        return length - o.length;
    }

    public void setChars(char[] chars, int length) {
        setChars(chars);
        setLength(length);
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }

    public void setLength(int length) {
        assert length <= chars.length && length >= 0;
        this.length = length;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
        assert cursorIsInBounds();
    }

    public void moveCursor(int dCursor) {
        this.cursor += dCursor;
        assert cursorIsInBounds();
    }

    private boolean cursorIsInBounds() {
        return cursor >= 0 && cursor <= length;
    }

    public int getCursor() {
        return cursor;
    }

    public char[] getChars() {
        return chars;
    }

    @Override
    public String toString() {
        return toDescriptiveString();
    }

    public String toStringValue() {
        return new String(chars, 0, length);
    }

    public String toDescriptiveString() {
        return "ParsableString{" +
                "chars.length=" + chars.length +
                "value=" + toStringValue() +
                ", length=" + length +
                ", cursor=" + cursor +
                '}';
    }
}
