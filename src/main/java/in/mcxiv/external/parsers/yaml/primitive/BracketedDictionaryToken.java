package in.mcxiv.external.parsers.yaml.primitive;

import in.mcxiv.external.parsers.SimpleNestableToken;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.RawLineToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.CollectionsToken;
import in.mcxiv.thatlang.natives.StringToken;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static in.mcxiv.parser.power.PowerUtils.*;

public class BracketedDictionaryToken extends SimpleNestableToken {

    private final HashMap<String, LinearValueToken> map;

    public BracketedDictionaryToken(Node parent, HashMap<String, LinearValueToken> map) {
        super(parent, map.values().toArray(Node[]::new));
        this.map = map;
    }

    public Map<String, LinearValueToken> getItems() {
        return map;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        List<Node> nodes = map.keySet().stream().map(s -> new Node(null, new StringToken(s), new Node(), map.get(s))).toList();
        return CollectionsToken.CollectionType.HASH_MAP.fromNodes(nodes, vm);
    }

    public static final class BracketedDictionaryParser implements Parser<BracketedDictionaryToken> {

        public static final Parser<BracketedDictionaryToken> bracketedDictionary = new BracketedDictionaryParser();

        private static final Parser<Node> pair = compound(new RawLineToken.RawLineParser(":"), inline(":"), LinearValueToken.LinearValueParser.linearValue);
        private static final TupleToken.TupleParser<Node, StringValueNode, TupleToken<Node>> tuple = new TupleToken.TupleParser<>(pair, word(","));

        private static final Parser<?> parser = compound(word("{"), inline(tuple), word("}"));

        @Override
        @SuppressWarnings("unchecked")
        public BracketedDictionaryToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            TupleToken<Node> tuple = (TupleToken<Node>) node.get(1);
            HashMap<String, LinearValueToken> map = new HashMap<>();
            tuple.getItems().forEach(n -> map.put(((RawLineToken) n.get(0)).getValue(), ((LinearValueToken) n.get(2))));
            return new BracketedDictionaryToken(parent, map);
        }
    }
}
