package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEOperatorsPrototype;

import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.inline;

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

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEOperatorsPrototype.operate(left.interpret(vm), operator, right.interpret(vm));
    }

    public static class BinaryOperatorParser implements Parser<BinaryOperatorToken> {

        String operator;
        private final Parser parser;

        public BinaryOperatorParser(String operator) {
            this.operator = operator;
            parser = compound(
                    SimpleSafeNonRecursiveExpressionParser.safeExpression,
                    inline(this.operator),
                    ExpressionsParser.expression
            );
        }

        @Override
        public BinaryOperatorToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            ExpressionsToken left = (ExpressionsToken) node.get(0);
            ExpressionsToken right = (ExpressionsToken) node.get(2);
            return new BinaryOperatorToken(left, operator, right);
        }
    }
}
