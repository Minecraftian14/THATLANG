package in.mcxiv.thatlang.natives;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.PowerUtils;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.utils.Cursors;
import in.mcxiv.utils.Strings;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

public class MultilineStringToken extends ExpressionsToken {

    private final String value;

    public MultilineStringToken(String value) {
        this(null, value);
    }

    public MultilineStringToken(Node parent, String value) {
        super(parent);
        this.value = value;
    }

    @Override
    public String toString() {
        return toExtendedString("value", value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.createValue(value);
    }

    public static class MultilineStringParser implements Parser<MultilineStringToken> {

        public static final Parser<?> multiLineString = PowerUtils.either(
                new MultilineStringParser("\"\"\"", "\"\"\""),
                new MultilineStringParser("\"\"", "\"\"")
        );

        protected final String starter;
        protected final String ender;

        protected MultilineStringParser(String starter, String ender) {
            this.starter = starter;
            this.ender = ender;
        }

        @Override
        public MultilineStringToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.matches(string, starter))
                return null;
            string.moveCursor(starter.length());
            String everything = Cursors.fetchEverythingUpTo(string, ender);
            if (everything == null) return null;
            string.moveCursor(ender.length());

            String[] lines = everything.split("[\n\r]");
            int maxSpaces = 0;
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].trim().matches("")) continue;
                int spaces = Strings.getIndentSpaces(lines[i]);
                if (maxSpaces < spaces) maxSpaces = spaces;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].length() > maxSpaces)
                    builder.append(lines[i].substring(maxSpaces)).append('\n');
                else builder.append(lines[i]).append('\n');
            }

            return new MultilineStringToken(parent, builder.toString());
        }
    }
}
