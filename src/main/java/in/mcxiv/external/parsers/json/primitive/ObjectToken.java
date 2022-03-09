package in.mcxiv.external.parsers.json.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.PowerUtils;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.CollectionsToken;
import thatlang.core.THATObject;

import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;

public final class ObjectToken extends SimpleNestableToken {
    public ObjectToken(Node parent, Node... children) {
        super(parent, children);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        TupleToken<?> node = getExp(TupleToken.class);
        if (node == null) return super.interpret(vm);
        List<Node> tokens = node.getItems().stream()
                .filter(PairToken.class::isInstance)
                .map(PairToken.class::cast)
                .map(pairToken -> new Node(null, pairToken.getName(), new Node(), pairToken.getValue()))
                .toList();
        return CollectionsToken.CollectionType.HASH_MAP.fromNodes(tokens, vm);
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
            return new ObjectToken(parent, node.get(1));
        }
    }
}
