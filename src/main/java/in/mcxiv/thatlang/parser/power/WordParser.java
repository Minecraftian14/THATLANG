package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.utils.Cursors;

public class WordParser implements Parser<StringValueNode> {

    protected final char[] chars;

    public WordParser(String string) {
        this.chars = string.toCharArray();
    }

    @Override
    public StringValueNode __parse__(ParsableString string, Node parent) {
        if (string.length() - string.getCursor() < chars.length) return null;
        for (char aChar : chars)
            if (aChar != Cursors.getCharAndNext(string))
                return null;
        return new StringValueNode(parent, new String(chars));
    }
}