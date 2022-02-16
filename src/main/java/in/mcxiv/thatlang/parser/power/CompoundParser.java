package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;

public class CompoundParser implements Parser<Node> {

    private final List<Parser<?>> parsers;

    public CompoundParser(Parser<?>...parsers) {
        this(List.of(parsers));
    }

    public CompoundParser(List<Parser<?>> parsers) {
        this.parsers = parsers;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
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
