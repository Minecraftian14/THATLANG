package in.mcxiv.parser.generic;

import in.mcxiv.parser.Parser;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Node;
import in.mcxiv.utils.Cursors;

public class SpacesToken extends StringValueNode {

    public SpacesToken(String value) {
        this(null, value);
    }

    public SpacesToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public String toString() {
        return toExtendedString("caught", getValue());
    }

    public static class SpacesParser implements Parser<SpacesToken> {
        public static SpacesParser spaces = new SpacesParser();

        @Override
        public SpacesToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.isSpace(string)) return null;
            StringBuilder builder = new StringBuilder();
            while (Cursors.bound(string) && Cursors.isSpace(string))
                builder.append(Cursors.getCharAndNext(string));
            return new SpacesToken(parent, builder.toString());
        }
    }
}
