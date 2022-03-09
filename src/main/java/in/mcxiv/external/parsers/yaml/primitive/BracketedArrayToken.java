package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.CollectionsToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.List;

import static in.mcxiv.external.parsers.yaml.primitive.LinearValueToken.LinearValueParser.linearValue;
import static in.mcxiv.parser.power.PowerUtils.*;
import static in.mcxiv.thatlang.natives.CollectionsToken.collectionToStringPort;

public class BracketedArrayToken extends SimpleNestableToken implements Interpretable<AbstractVM, THATObject> {
    public BracketedArrayToken(Node parent, LinearValueToken... children) {
        super(parent, children);
    }

    public List<LinearValueToken> getItems() {
        return getChildren(LinearValueToken.class);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return CollectionsToken.CollectionType.ARRAY_LIST.fromNodes(getChildren(), vm);
    }

    public static final class BracketedArrayParser implements Parser<BracketedArrayToken> {

        public static final Parser<BracketedArrayToken> bracketedArray = new BracketedArrayParser();

        private static final TupleToken.TupleParser<LinearValueToken, StringValueNode, TupleToken<LinearValueToken>>
                tuple = new TupleToken.TupleParser<>(linearValue, word(","));

        private static final Parser<?> parser = compound(word("["), inline(tuple), word("]"));

        @Override
        @SuppressWarnings("unchecked")
        public BracketedArrayToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            TupleToken<LinearValueToken> tuple = (TupleToken<LinearValueToken>) node.get(1);
            return new BracketedArrayToken(parent, tuple.getItems().toArray(LinearValueToken[]::new));
        }
    }
}
