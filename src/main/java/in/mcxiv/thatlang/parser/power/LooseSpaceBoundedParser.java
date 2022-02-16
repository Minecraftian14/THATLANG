package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.utils.Cursors;

public class LooseSpaceBoundedParser implements Parser<Node> {

    Parser<?> parser;

    public LooseSpaceBoundedParser(String string) {
        this(new WordParser(string));
    }

    public LooseSpaceBoundedParser(Parser<?> parser) {
        this.parser = parser;
    }

    @Override
    public Node __parse__(ParsableString string, Node parent) {
//        if (!Cursors.isSpace(string) && Cursors.getChar(string) != chars[0]) return null;
        while (Cursors.bound(string) && Cursors.isSpace(string)) string.moveCursor(1);

        Node value = parser.parse(string, parent);
        if (value == null) return null;

        while (Cursors.bound(string) && Cursors.isSpace(string)) string.moveCursor(1);

        return value;
    }
}