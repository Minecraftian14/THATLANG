package in.mcxiv.parser.power;

import in.mcxiv.parser.Parser;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Node;

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
