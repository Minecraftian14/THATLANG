package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.tree.Node;

public class MinimumRepeatableParser extends RepeatableParser {

    int minimumRepeats;

    public MinimumRepeatableParser(int minimumRepeats, Parser parser) {
        super(parser);
        this.minimumRepeats = minimumRepeats;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        Node node = super.__parse__(string, parent);
        if (node == null) return null;
        if (node.noOfChildren() < minimumRepeats) {
            node.delete();
            return null;
        }
        return node;
    }
}
