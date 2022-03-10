package in.mcxiv.external.parsers.json.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NumeralToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.StringToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import static in.mcxiv.parser.power.PowerUtils.either;
import static in.mcxiv.parser.power.PowerUtils.word;

public final class ValueToken extends SimpleNestableToken {
    public ValueToken(Node parent, Node... children) {
        super(parent, children);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        Node tree = get(0);
        if (tree instanceof StringToken st) return THOSEObjects.createValue(st.getValue());
        if (tree instanceof NumeralToken nt) return THOSEObjects.createAfterReducing(nt.getValue());
        if (tree instanceof ObjectToken ot) return ot.interpret(vm);
        if (tree instanceof ArrayToken at) return at.interpret(vm);
        if (tree instanceof StringValueNode svn)
            return THOSEObjects.createValue(switch (svn.getValue()) {
                case "true" -> true;
                case "false" -> false;
                case "null" -> null;
                default -> throw new IllegalStateException("If not what done above, then what is tree's class type? tree.class=" + tree.getClass().getName());
            });
        throw new IllegalStateException("If not what done above, then what is tree's class type? tree.class=" + tree.getClass().getName());
    }

    public static final class ValueParser implements Parser<ValueToken> {
        public static final Parser<ValueToken> value = new ValueParser();

        private static final Parser<?> parser = either(
                StringToken.StringParser.string,
                NumeralToken.NumeralParser.numeral,
                ObjectToken.ObjectParser.object,
                ArrayToken.ArrayParser.array,
                word("true"),
                word("false"),
                word("null")
        );

        private ValueParser() {
        }

        @Override
        public ValueToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new ValueToken(parent, node);
        }
    }
}
