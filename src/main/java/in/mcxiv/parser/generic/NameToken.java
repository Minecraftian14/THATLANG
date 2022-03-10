package in.mcxiv.parser.generic;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class NameToken extends StringValueNode {

    public NameToken(String value) {
        this(null, value);
    }

    public NameToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public String toString() {
        return "\"NameToken\":\"" + getValue() + "\"";
    }

    public static class NameParser implements Parser<NameToken> {
        public static NameParser name = new NameParser();

        private NameParser() {
        }

        @Override
        public NameToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.isNameStarter(string)) return null;
            int start = string.getCursor();
            do string.moveCursor(1);
            while (Cursors.bound(string) && Cursors.isNameContent(string));
            return new NameToken(parent, string.subSequenceS(start, string.getCursor() - start));
        }
    }
}
