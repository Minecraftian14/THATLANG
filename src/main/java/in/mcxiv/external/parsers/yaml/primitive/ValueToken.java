package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.tryCatchSuite.Try;
import thatlang.core.THATObject;

import static in.mcxiv.parser.power.PowerUtils.either;

public final class ValueToken extends SimpleNestableToken {
    public ValueToken(Node parent, Node... children) {
        super(parent, children);
    }

    public Object getValue() {
        Node node = get(0);
        if (node instanceof MultilineStringToken mst) return mst.getValue();
        if (node instanceof MultilineStringWithNewLinesToken mst) return mst.getValue();
        if (node instanceof BracketedArrayToken bat) return bat.getItems();
        if (node instanceof BracketedDictionaryToken bdt) return bdt.getItems();
        if (node instanceof MultilineArrayStructureToken mast) return mast.getItems();
        if (node instanceof MultilineDictionaryStructureToken mdst) return mdst.getItems();
        if (node instanceof LinearValueToken lvt) return lvt.getValue();
        throw new IllegalStateException("shouldn't %s be caught in Raw Line Token?".formatted(node));
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        Node node = get(0);
        //noinspection unchecked
        return Try.GetAnd(() -> ((Interpretable<AbstractVM, THATObject>) node).interpret(vm)).ElseThrow();
    }

    @Override
    public String toString() {
        return "\"ValueToken\\" + get(0);
    }

    public static final class ValueParser implements Parser<ValueToken> {
        public static final Parser<ValueToken> value = new ValueParser();

        private static final Parser<?> parser = either(
                MultilineStringToken.MultilineStringParser.multilineString,
                MultilineStringWithNewLinesToken.MultilineStringParser.multilineString,
                BracketedArrayToken.BracketedArrayParser.bracketedArray,
                BracketedDictionaryToken.BracketedDictionaryParser.bracketedDictionary,
                MultilineArrayStructureToken.MultilineArrayStructureParser.multilineArray,
                MultilineDictionaryStructureToken.MultilineDictionaryStructureParser.multilineDictionary,
                LinearValueToken.LinearValueParser.linearValue
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
