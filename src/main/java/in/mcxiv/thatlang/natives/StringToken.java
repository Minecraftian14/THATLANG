package in.mcxiv.thatlang.natives;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.utils.Cursors;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

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
        return "\"StringToken\":\"" + value + "\"";
    }

    public String getValue() {
        return value;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.createValue(value);
    }

    public static class StringParser implements Parser<StringToken> {

        public static final StringParser string = new StringParser();

        private StringParser() {
        }

        @Override
        public StringToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != '"') return null;
            int start = string.getCursor();

            boolean flag_isEscape = false;
            while (Cursors.bound(string)) {
                char c = Cursors.getCharAndNext(string);
                if (flag_isEscape) {
                    flag_isEscape = false;
                    continue;
                }
                if (c == '\\') flag_isEscape = true;
                else if (c == '"') break;
            }
            return new StringToken(parent, (String) string.subSequence(start, string.getCursor() - start - 1));
        }
    }
}
