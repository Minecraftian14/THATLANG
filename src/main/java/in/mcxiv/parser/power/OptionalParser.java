package in.mcxiv.parser.power;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.Node;
import in.mcxiv.utils.Cursors;

public class OptionalParser implements Parser<Node> {

    final Parser<?> parser;

    public OptionalParser(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public Node parse(ParsableString string, Node parent) {
        if(!Cursors.bound(string)) return new Node(parent);
        return Parser.super.parse(string, parent);
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        return optional(string,parent,parser);
    }

    public static Node optional(ParsableString string, Node parent, Parser<?> parser) {
        Node element = parser.parse(string, parent);
        if(element == null) return new Node(parent);
        return element;
    }

}
