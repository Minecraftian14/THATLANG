package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.tree.Node;

/**
 * `operand operator operand` ie things like 1+3 gesuga()*78...
 */
public class BinaryOperatorToken extends ExpressionsToken {
    ExpressionsToken left;
    String operator;
    ExpressionsToken right;

    public BinaryOperatorToken(ExpressionsToken left, String operator, ExpressionsToken right) {
        this(null, left, operator, right);
    }

    public BinaryOperatorToken(Node parent, ExpressionsToken left, String operator, ExpressionsToken right) {
        super(parent);
        this.left = left;
        this.operator = operator;
        this.right = right;
        addChild(left);
        addChild(right);
    }

    public ExpressionsToken getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public ExpressionsToken getRight() {
        return right;
    }

    @Override
    public String toString() {
        return toExtendedString("left", left, "operator", operator, "right", right);
    }

    public static class BinaryOperatorParser implements Parser<BinaryOperatorToken> {

        String operator;
        private final Parser parser;

        public BinaryOperatorParser(String operator) {
            this.operator = operator;
            parser = new CompoundParser(
                    QuantaExpressionToken.QuantaExpressionParser.instance,
                    new LooseSpaceBoundedParser(this.operator),
                    ExpressionsParser.instance
            );
        }

        @Override
        public BinaryOperatorToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            ExpressionsToken left = (ExpressionsToken) node.get(0);
            ExpressionsToken right = (ExpressionsToken) node.get(2);
            return new in.mcxiv.thatlang.parser.expression.BinaryOperatorToken(left, operator, right);
        }
    }
}
