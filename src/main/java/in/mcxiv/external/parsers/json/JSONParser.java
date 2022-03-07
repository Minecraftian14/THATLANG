package in.mcxiv.external.parsers.json;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NumeralToken.NumeralParser;
import in.mcxiv.parser.power.PowerUtils;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.natives.StringToken.StringParser;

import static in.mcxiv.parser.power.PowerUtils.*;

public class JSONParser implements Parser<JSONNode> {

    public static final Parser<JSONNode> json = new JSONParser();

    public static sealed class SimpleNestableToken extends Node {
        public SimpleNestableToken(Node parent, Node... children) {
            super(parent);
            for (Node child : children) addChild(child);
        }
    }

    public static final class ObjectToken extends SimpleNestableToken {
        public ObjectToken(Node parent, Node... children) {
            super(parent, children);
        }

        public static final class ObjectParser implements Parser<ObjectToken> {
            public static final Parser<ObjectToken> object = new ObjectParser();
            private static final Parser<?> content = new TupleToken.TupleParser<>(PairToken.PairParser.pair, word(","), PowerUtils::block);
            private static final Parser<?> parser = compound(word("{"), optional(block(content)), word("}"));

            private ObjectParser() {
            }

            @Override
            public ObjectToken __parse__(ParsableString string, Node parent) {
                Node node = parser.parse(string);
                if (node == null) return null;
                return new ObjectToken(parent, node);
            }
        }
    }

    public static final class PairToken extends SimpleNestableToken {
        public PairToken(Node parent, Node... children) {
            super(parent, children);
        }

        public static final class PairParser implements Parser<PairToken> {
            public static final Parser<PairToken> pair = new PairParser();
            private static final Parser<?> parser = compound(
                    StringParser.string,
                    inline(":"),
                    ValueToken.ValueParser.value
            );

            private PairParser() {
            }

            @Override
            public PairToken __parse__(ParsableString string, Node parent) {
                Node node = parser.parse(string);
                if (node == null) return null;
                return new PairToken(parent, node);
            }
        }
    }

    public static final class ArrayToken extends SimpleNestableToken {
        public ArrayToken(Node parent, Node... children) {
            super(parent, children);
        }

        public static final class ArrayParser implements Parser<ArrayToken> {
            public static final Parser<ArrayToken> array = new ArrayParser();
            private static final Parser<?> content = new TupleToken.TupleParser<>(ValueToken.ValueParser.value, word(","), PowerUtils::block);
            private static final Parser<?> parser = compound(word("["), optional(block(content)), word("]"));

            private ArrayParser() {
            }

            @Override
            public ArrayToken __parse__(ParsableString string, Node parent) {
                Node node = parser.parse(string);
                if (node == null) return null;
                return new ArrayToken(parent, node);
            }
        }
    }

    public static final class ValueToken extends SimpleNestableToken {
        public ValueToken(Node parent, Node... children) {
            super(parent, children);
        }

        public static final class ValueParser implements Parser<ValueToken> {
            public static final Parser<ValueToken> value = new ValueParser();

            private static final Parser<?> parser = either(
                    StringParser.string,
                    NumeralParser.numeral,
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

    private static final Parser<?> parser = ValueToken.ValueParser.value;

    private JSONParser() {
    }

    @Override
    public JSONNode __parse__(ParsableString string, Node parent) {
        Node node = parser.parse(string);
        if (node == null) return null;
        return new JSONNode(parent, node);
    }
}
