package in.mcxiv.parser.generic;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.PowerUtils;

public class ValueToken extends StringValueNode {

    public ValueToken(String value) {
        this(null, value);
    }

    public ValueToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public String toString() {
        return "value:" + super.toString();
    }

    public static class ValueParser implements Parser<ValueToken> {
        public static ValueParser value = new ValueParser();

        private static final Parser<?> parser = PowerUtils.either(NameToken.NameParser.name, NumeralToken.NumeralParser.numeral);

        private ValueParser() {
        }

        @Override
        public ValueToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new ValueToken(parent, ((StringValueNode) node).getValue());
        }
    }
}
