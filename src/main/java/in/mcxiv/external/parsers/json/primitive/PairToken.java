package in.mcxiv.external.parsers.json.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.StringToken;
import thatlang.core.THATObject;

import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.inline;

public final class PairToken extends SimpleNestableToken {
    public PairToken(Node parent, Node... children) {
        super(parent, children);
    }

    public StringToken getName() {
        return getExp(StringToken.class);
    }

    public ValueToken getValue() {
        return getExp(ValueToken.class);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        throw new IllegalStateException();
    }

    public static final class PairParser implements Parser<PairToken> {
        public static final Parser<PairToken> pair = new PairParser();
        private static final Parser<?> parser = compound(
                StringToken.StringParser.string,
                inline(":"),
                ValueToken.ValueParser.value
        );

        private PairParser() {
        }

        @Override
        public PairToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new PairToken(parent, node.get(0), node.get(2));
        }
    }
}
