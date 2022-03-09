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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.inline;

public class MultilineDictionaryStructureToken extends SimpleNestableToken {

    private final HashMap<String, ValueToken> map;

    public MultilineDictionaryStructureToken(Node parent, HashMap<String, ValueToken> map) {
        super(parent, map.values().toArray(Node[]::new));
        this.map = map;
    }

    public Map<String, ValueToken> getItems() {
        return map;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        List<Node> nodes = map.keySet().stream().map(s -> new Node(null, new StringToken(s), new Node(), map.get(s))).toList();
        return CollectionsToken.CollectionType.HASH_MAP.fromNodes(nodes, vm);
    }

    public static final class MultilineDictionaryStructureParser implements Parser<MultilineDictionaryStructureToken> {

        public static final Parser<MultilineDictionaryStructureToken> multilineDictionary = new MultilineDictionaryStructureParser();

        private static final Parser<Node> pair = compound(new RawLineToken.RawLineParser(":"), inline(":"), ValueToken.ValueParser.value);

        private static final Parser<?> parser = new BlockInIndentsParser("", pair);

        private MultilineDictionaryStructureParser() {
        }

        @Override
        public MultilineDictionaryStructureToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;

            HashMap<String, ValueToken> map = new HashMap<>();
            node.getChildren().forEach(n -> map.put(((RawLineToken) n.get(0)).getValue(), ((ValueToken) n.get(2))));
            return new MultilineDictionaryStructureToken(parent, map);
        }
    }
}
