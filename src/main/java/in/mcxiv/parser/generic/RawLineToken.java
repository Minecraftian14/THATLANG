package in.mcxiv.parser.generic;

import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.utils.Cursors;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

public class RawLineToken extends StringValueNode implements Interpretable<AbstractVM, THATObject> {
    public RawLineToken(String value) {
        super(value);
    }

    public RawLineToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.createValue(getValue());
    }

    public static final class RawLineParser implements Parser<RawLineToken> {

        public static final Parser<RawLineToken> rawLine = new RawLineParser();

        private String limit;

        private RawLineParser() {
            this("\n");
        }

        public RawLineParser(String limit) {
            this.limit = limit;
        }

        @Override
        public RawLineToken __parse__(ParsableString string, Node parent) {
            if (Cursors.isBlank(string)) return null;
            String line = Cursors.fetchEverythingUpTo(string, limit);
            if (line == null) return null;
            return new RawLineToken(parent, line);
        }
    }
}
