package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.tree.Node;

public class ExpressionsToken extends Node {

    public ExpressionsToken() {
    }

    public ExpressionsToken(Node parent) {
        super(parent);
    }

    public static class ExpressionsParser implements Parser<ExpressionsToken> {
        public static final ExpressionsParser instance = new ExpressionsParser();

        private static final Parser parser = new EitherParser(
                MultiOperandParser.instance,
                ParenthesisParser.instance,
                QuantaExpressionToken.QuantaExpressionParser.instance
                );

        private ExpressionsParser() {
        }

        @Override
        public ExpressionsToken __parse__(ParsableString string, Node parent) {
            return ((ExpressionsToken) parser.parse(string, parent));
        }
    }
}
