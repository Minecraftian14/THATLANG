package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.tryCatchSuite.Try;
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
        Node element = parser.parse(string, parent);
        if(element == null) return new Node(parent);
        return element;
    }
}
