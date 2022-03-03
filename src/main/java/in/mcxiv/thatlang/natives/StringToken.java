package in.mcxiv.thatlang.natives;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.parser.Node;
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
        return toExtendedString("value", value);
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
            while (Cursors.bound(string) && Cursors.getCharAndNext(string) != '"') ;
//            if (Cursors.getChar(string) != '"') return null;
            return new StringToken(parent, ((ParsableString) string.subSequence(start, string.getCursor() - 1)).toStringValue());
        }
    }
}
