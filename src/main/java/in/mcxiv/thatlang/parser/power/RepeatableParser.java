package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.tree.Node;

public class RepeatableParser implements Parser<Node> {

    private final CompoundParser parser;

    public RepeatableParser(Parser<?>... parsers) {
        this.parser = new CompoundParser(parsers);
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        parent = new Node(parent);

        while (true) {
            Node node = parser.parse(string, parent);//
            if (node == null) {
                if (parent.noOfChildren() == 0) {
                    parent.delete();
                    return null;
                }
                return parent;
            }
        }
    }
}
