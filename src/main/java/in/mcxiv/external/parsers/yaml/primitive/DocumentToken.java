package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.RawLineToken;
import in.mcxiv.parser.power.BlockInIndentsParser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.CollectionsToken;
import in.mcxiv.thatlang.natives.StringToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;

public class DocumentToken extends SimpleNestableToken {

    public DocumentToken(Node parent, Node... children) {
        super(parent, children);
    }

    public int noOfFields() {
        return noOfChildren();
    }

    public ValueToken getField(String name) {
        for (Node child : getChildren())
            if (name.equals(((RawLineToken) child.get(0)).getValue()))
                return (ValueToken) child.get(2);
        return null;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        List<Node> nodes = getChildren().stream().map(node -> new Node(null, node.get(0), new Node(), node.get(2))).toList();
        return CollectionsToken.CollectionType.HASH_MAP.fromNodes(nodes, vm);
    }

    public static final class DocumentParser implements Parser<DocumentToken> {

        public static final Parser<DocumentToken> document = new DocumentParser();

        private static final Parser<?> line = compound(new RawLineToken.RawLineParser(":"), inline(":"), ValueToken.ValueParser.value);
        private static final Parser<?> parser = compound(new BlockInIndentsParser("---", line), optional(block(word(""))), word("..."));

        private DocumentParser() {
        }

        @Override
        public DocumentToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new DocumentToken(parent, node.get(0).getChildren().toArray(Node[]::new));
        }
    }
}
