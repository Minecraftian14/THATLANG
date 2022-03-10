package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NumeralToken;
import in.mcxiv.parser.generic.RawLineToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.StringToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import static in.mcxiv.parser.power.PowerUtils.either;
import static in.mcxiv.parser.power.PowerUtils.word;

public final class LinearValueToken extends SimpleNestableToken {
    public LinearValueToken(Node parent, Node... children) {
        super(parent, children);
    }

    public Object getValue() {
        Node node = get(0);
        if (node instanceof StringToken st) return st.getValue();
        if (node instanceof MultilineStringToken mst) return mst.getValue();
        if (node instanceof MultilineStringWithNewLinesToken mst) return mst.getValue();
        if (node instanceof NumeralToken nt) return nt.reduceToNumber();
        if (node instanceof RawLineToken rlt) return rlt.getValue();
        if (node instanceof StringValueNode svn)
            return switch (svn.getValue().toLowerCase()) {
                case ".inf" -> Double.POSITIVE_INFINITY;
                case "-.inf" -> Double.NEGATIVE_INFINITY;
                case ".nan" -> Double.NaN;
                case "true", "on" -> true;
                case "false", "off" -> false;
                case "null", "~" -> null;
                default -> throw new IllegalStateException("shouldn't %s be caught in Raw Line Token?".formatted(svn.getValue()));
            };
        throw new IllegalStateException("shouldn't %s be caught in Raw Line Token?".formatted(node));
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.createValue(getValue());
    }

    @Override
    public String toString() {
        return "\"LinearValueToken\\" + get(0);
    }

    public static final class LinearValueParser implements Parser<LinearValueToken> {
        public static final Parser<LinearValueToken> linearValue = new LinearValueParser();

        private static final Parser<?> parser = either(
                StringToken.StringParser.string,
                word(".inf"),// TODO: make'em case insensitive
                word("-.inf"),
                word(".nan"),
                word("true"),
                word("false"),
                word("on"),
                word("off"),
                word("~"),
                word("null"),
                NumeralToken.NumeralParser.numeral,
                RawLineToken.RawLineParser.rawLine
        );

        private LinearValueParser() {
        }

        @Override
        public LinearValueToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new LinearValueToken(parent, node);
        }
    }
}
