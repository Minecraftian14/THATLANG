package in.mcxiv.parser.power;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;

import java.util.List;

public class CompoundParser implements Parser<Node> {

    private final List<Parser<?>> parsers;

    public CompoundParser(Parser<?>... parsers) {
        this(List.of(parsers));
    }

    public CompoundParser(List<Parser<?>> parsers) {
        this.parsers = parsers;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        return compound(string, parent, parsers);
    }

    public static Node compound(ParsableString string, Node parent, List<Parser<?>> parsers) {
        parent = new Node(parent);
        for (Parser<?> parser : parsers) {
            Node node = parser.parse(string, parent);
            if (node == null) {
                parent.delete();
                return null;
            }
        }
        return parent;
    }
}
