package in.mcxiv.parser.power;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.Node;

public class RepeatableParser implements Parser<Node> {

    private final Parser<?> parser;

    public RepeatableParser(Parser<?> parser) {
        this.parser = parser;
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
