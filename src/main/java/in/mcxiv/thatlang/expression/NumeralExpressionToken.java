package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NumeralToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

public class NumeralExpressionToken extends ExpressionsToken {
    String value;

    public NumeralExpressionToken(String value) {
        this(null, value);
    }

    public NumeralExpressionToken(Node parent, String value) {
        super(parent);
        this.value = value;
    }

    @Override
    public String toString() {
        return toExtendedString("value", value);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.createAfterReducing(value);
    }

    public static class NumeralExpressionParser implements Parser<NumeralExpressionToken> {

        public static final Parser<NumeralExpressionToken> numericalExpr = new NumeralExpressionParser();

        private NumeralExpressionParser() {
        }

        @Override
        public NumeralExpressionToken __parse__(ParsableString string, Node parent) {
            NumeralToken token = NumeralToken.NumeralParser.numeral.parse(string);
            if (token == null) return null;
            return new NumeralExpressionToken(parent, token.getValue());
        }
    }
}
