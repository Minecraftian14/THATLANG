package in.mcxiv.thatlang.parser.natives;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.utils.Cursors;

public class StringToken extends ExpressionsToken {

    private final String value;

    public StringToken(String value) {
        this(null, value);
    }

    public StringToken(Node parent, String value) {
        super(parent);
        this.value = value;
    }

    @Override
    public String toString() {
        return toExtendedString("value", value);
    }

    public String getValue() {
        return value;
    }

    public static class StringParser implements Parser<StringToken> {

        public static final StringParser instance = new StringParser();

        private StringParser() {
        }

        @Override
        public StringToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != '"') return null;
            int start = string.getCursor();
            while (Cursors.bound(string) && Cursors.getCharAndNext(string) != '"') ;
//            if (Cursors.getChar(string) != '"') return null;
            return new StringToken(parent, ((ParsableString) string.subSequence(start, string.getCursor() - 1)).toStringValue());
        }
    }
}
