package in.mcxiv.parser.generic;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class NumeralToken extends StringValueNode {

    public NumeralToken(String value) {
        this(null, value);
    }

    public NumeralToken(Node parent, String value) {
        super(parent, value);
    }

    @Override
    public String toString() {
        return "numeral = " + getValue();
    }

    public static class NumeralParser implements Parser<NumeralToken> {
        public static NumeralParser numeral = new NumeralParser();

        @Override
        public NumeralToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.isNumericStarter(string)) return null;

            String base = parseANumber(string);
            if (base == null) return null;

            if (!Cursors.bound(string) || Cursors.getChar(string) != 'e')
                return new NumeralToken(parent, base);
            string.moveCursor(1);

            String exponent = parseANumber(string);
            if (exponent == null) return null;

            return new NumeralToken(parent, base + 'e' + exponent);
        }

        private static String parseANumber(ParsableString string) {
            int start = string.getCursor();

            if (!Cursors.isNumericStarter(string)) return null;
            string.moveCursor(1);

            while (Cursors.bound(string) && Cursors.isNumericContent(string))
                string.moveCursor(1);

            if (!Cursors.bound(string) || Cursors.getChar(string) != '.')
                return string.subSequenceS(start, string.getCursor() - start);
            string.moveCursor(1);

            while (Cursors.bound(string) && Cursors.isNumericContent(string))
                string.moveCursor(1);

            return string.subSequenceS(start, string.getCursor() - start);
        }
    }
}
