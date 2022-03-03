package in.mcxiv.parser.power;

import in.mcxiv.parser.Parser;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Node;
import in.mcxiv.utils.Cursors;

public class LooseBlockParser implements Parser<Node> {

    Parser<?> parser;

    public LooseBlockParser(String string) {
        this(new WordParser(string));
    }

    public LooseBlockParser(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        while (Cursors.bound(string) && Cursors.isWhite(string)) string.moveCursor(1);

        Node value = parser.parse(string, parent);
        if (value == null) return null;

        while (Cursors.bound(string) && Cursors.isWhite(string)) string.moveCursor(1);

        return value;
    }
}