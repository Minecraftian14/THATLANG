package in.mcxiv.parser.power;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class LooseInlineParser implements Parser<Node> {

    Parser<?> parser;

    public LooseInlineParser(String string) {
        this(new WordParser(string));
    }

    public LooseInlineParser(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
        return inline(string, parent, parser);
    }

    public static Node inline(ParsableString string, Node parent, Parser<?> parser) {
        while (Cursors.bound(string) && Cursors.isSpace(string)) string.moveCursor(1);

        Node value = parser.parse(string, parent);
        if (value == null) return null;

        while (Cursors.bound(string) && Cursors.isSpace(string)) string.moveCursor(1);

        return value;
    }
}