package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;

public class EitherParser implements Parser<Node> {

    private final List<Parser<?>> parsers;

    public EitherParser(Parser<?>... parsers) {
        this(List.of(parsers));
    }

    public EitherParser(List<Parser<?>> parsers) {
        this.parsers = parsers;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        for (Parser<?> parser : parsers) {
            Node node = parser.parse(string, parent);
            if (node != null)
                return node;
        }
        return null;
    }
}
