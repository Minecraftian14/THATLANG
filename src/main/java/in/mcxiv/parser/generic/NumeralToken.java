package in.mcxiv.parser.generic;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;
import in.mcxiv.utils.PrimitiveParser;

public class NumeralToken extends StringValueNode {

    String positivity;
    String base;
    String exponent;
    String solidType;

    public NumeralToken(String positivity, String base, String exponent, String solidType) {
        this(null, positivity, base, exponent, solidType);
    }

    public NumeralToken(Node parent, String base) {
        this(parent, "", base, "", "");
    }

    public NumeralToken(Node parent, String positivity, String base, String exponent, String solidType) {
        super(parent, positivity + base + (exponent.isEmpty() ? "" : "e") + reducedExponent(exponent) + reducedSolidType(solidType));
        this.positivity = positivity;
        this.base = base;
        this.exponent = exponent;
        this.solidType = solidType;
    }

    private static String reducedExponent(String exponent) {
        if (exponent.contains(".")) return exponent.substring(0, exponent.indexOf('.'));
        return exponent;
    }

    private static String reducedSolidType(String solidType) {
        if (solidType.equals("")) return "";
        if (solidType.length() > 1) solidType = solidType.substring(0, 1);
        if ("ldfLDF".indexOf(solidType.charAt(0)) != 1) return solidType;
        return "";
    }

    public String getPositivity() {
        return positivity;
    }

    public String getBase() {
        return base;
    }

    public String getExponent() {
        return exponent;
    }

    public String getSolidType() {
        return solidType;
    }

    public String getRawNumber() {
        return positivity + base + "e" + exponent + solidType;
    }

    public Number reduceToNumber() {
        PrimitiveParser<? extends Number> parser = getSuitableParser();
        if (parser != null) return parser.parse(getValue());
        if (!base.contains(".") && exponent.equals("")) return PrimitiveParser.INT.parse(positivity + base);
        double b = PrimitiveParser.DOUBLE.parse(positivity + base);
        double e = PrimitiveParser.DOUBLE.parse(exponent);
        return b * Math.pow(10.0, e);
    }

    public PrimitiveParser<? extends Number> getSuitableParser() {
        return switch (solidType.toLowerCase()) {
            case "s" -> PrimitiveParser.SHORT;
            case "l" -> PrimitiveParser.LONG;
            case "d" -> PrimitiveParser.DOUBLE;
            case "f" -> PrimitiveParser.FLOAT;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return "\"NumeralToken\":\"" + getValue() + "\"";
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

            String solidType = "";
            if (Cursors.bound(string) && Cursors.isNumericSingletonEnding(string))
                solidType = "" + Cursors.getCharAndNext(string);

            return new NumeralToken(parent, "", base, exponent, solidType);
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
