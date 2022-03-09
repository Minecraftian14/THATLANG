package in.mcxiv.parser.power;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.utils.Cursors;

public class WordParser implements Parser<StringValueNode> {

    protected final char[] chars;

    public WordParser(String string) {
        this.chars = string.toCharArray();
    }

    @Override
    public StringValueNode __parse__(ParsableString string, Node parent) {
        return word(string, parent, chars);
    }


    public static StringValueNode word(ParsableString string, Node parent, char[] chars) {
        if (string.length() - string.getCursor() < chars.length) return null;
        for (char aChar : chars)
            if (aChar != Cursors.getCharAndNext(string))
                return null;
        return new StringValueNode(parent, new String(chars));
    }

}