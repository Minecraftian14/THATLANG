package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.EitherParser;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import interpreter.Interpretable;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

public class ExpressionsToken extends Node implements Interpretable<AbstractVM, THATObject> {

    public ExpressionsToken() {
    }

    public ExpressionsToken(Node parent) {
        super(parent);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.NULL;
    }

    public static class ExpressionsParser implements Parser<ExpressionsToken> {
        public static final ExpressionsParser expression = new ExpressionsParser();

        private static final Parser parser = new EitherParser(
                MultiOperandParser.multiOperand,
                SimpleSafeNonRecursiveExpressionParser.safeExpression
                );

        private ExpressionsParser() {
        }

        @Override
        public ExpressionsToken __parse__(ParsableString string, Node parent) {
            return ((ExpressionsToken) parser.parse(string, parent));
        }
    }
}
