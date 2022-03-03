package in.mcxiv.parser.generic;

import in.mcxiv.parser.Parser;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Node;
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
        return toExtendedString("name", getValue());
    }

    public static class NameParser implements Parser<NameToken> {
        public static NameParser name = new NameParser();

        @Override
        public NameToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.isLetterOrDigit(string)) return null;
            StringBuilder builder = new StringBuilder();
            while (Cursors.bound(string) && Cursors.isLetterOrDigit(string))
                builder.append(Cursors.getCharAndNext(string));
            return new NameToken(parent, builder.toString());
        }
    }
}
