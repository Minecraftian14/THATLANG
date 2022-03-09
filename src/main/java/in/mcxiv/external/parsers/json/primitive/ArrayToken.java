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

public final class ArrayToken extends SimpleNestableToken {
    public ArrayToken(Node parent, Node... children) {
        super(parent, children);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        TupleToken<?> node = getExp(TupleToken.class);
        if (node == null) return super.interpret(vm);
        List<Node> values = node.getItems().stream().map(Node.class::cast).toList();
        return CollectionsToken.CollectionType.ARRAY_LIST.fromNodes(values, vm);
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
            return new ArrayToken(parent, node.get(1));
        }
    }
}
