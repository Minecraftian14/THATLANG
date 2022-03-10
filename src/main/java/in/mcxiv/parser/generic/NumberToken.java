package in.mcxiv.parser.generic;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class NumberToken extends StringValueNode {

    public NumberToken(String value) {
        this(null, value);
    }

    public NumberToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public String toString() {
        return toExtendedString("name", getValue());
    }

    public static class NameParser implements Parser<NumberToken> {
        public static NameParser name = new NameParser();

        @Override
        public NumberToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.isValueTokenCharacter(string)) return null;
            StringBuilder builder = new StringBuilder();
            while (Cursors.bound(string) && Cursors.isValueTokenCharacter(string))
                builder.append(Cursors.getCharAndNext(string));
            return new NumberToken(parent, builder.toString());
        }
    }
}
