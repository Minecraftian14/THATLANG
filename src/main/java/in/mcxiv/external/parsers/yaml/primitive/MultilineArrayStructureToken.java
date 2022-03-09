package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.SpacesToken;
import in.mcxiv.parser.power.BlockInIndentsParser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.CollectionsToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;
import static in.mcxiv.thatlang.natives.CollectionsToken.collectionToStringPort;

public class MultilineArrayStructureToken extends SimpleNestableToken {

    public MultilineArrayStructureToken(Node parent, ValueToken... children) {
        super(parent, children);
    }

    public List<ValueToken> getItems() {
        return getChildren(ValueToken.class);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return CollectionsToken.CollectionType.ARRAY_LIST.fromNodes(getChildren(), vm);
    }

    public static final class MultilineArrayStructureParser implements Parser<MultilineArrayStructureToken> {

        public static final Parser<MultilineArrayStructureToken> multilineArray = new MultilineArrayStructureParser();

        private static final Parser<Node> item = compound(word("-"), optional(SpacesToken.SpacesParser.spaces), ValueToken.ValueParser.value);

        private static final Parser<?> parser = new BlockInIndentsParser("", item);

        private MultilineArrayStructureParser() {
        }

        @Override
        public MultilineArrayStructureToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            if (node.noOfChildren() == 0) return null;
            return new MultilineArrayStructureToken(parent, node.getChildren().stream()
                    .map(n -> n.get(2))
                    .filter(n -> n instanceof ValueToken)
                    .toArray(ValueToken[]::new));
        }
    }
}
