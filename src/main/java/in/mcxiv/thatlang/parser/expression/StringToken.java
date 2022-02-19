package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.utils.Cursors;

public class StringToken extends StringValueNode {

    public StringToken(String value) {
        this(null, value);
    }

    public StringToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public String toString() {
        return toExtendedString("function name", getValue());
    }

    public static class StringParser implements Parser<StringToken> {

        public static final StringParser instance = new StringParser();

        private StringParser() {
        }

        @Override
        public StringToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != '"') return null;
            int start = string.getCursor();
            while (Cursors.bound(string) && Cursors.getCharAndNext(string) != '"');
//            if (Cursors.getChar(string) != '"') return null;
            return new StringToken(parent, ((ParsableString) string.subSequence(start, string.getCursor() - 1)).toStringValue());
        }
    }
}
