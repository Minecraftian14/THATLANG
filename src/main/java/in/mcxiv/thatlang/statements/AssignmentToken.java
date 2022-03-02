package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.parser.tree.Node;

import static in.mcxiv.thatlang.parser.expression.QuantaExpressionToken.QuantaExpressionParser.quantaExpression;
import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

public class AssignmentToken extends StatementToken {

    QuantaExpressionToken field;
    ExpressionsToken expression;

    public AssignmentToken(QuantaExpressionToken field, ExpressionsToken expression) {
        this(null, field, expression);
    }

    public AssignmentToken(Node parent, QuantaExpressionToken field, ExpressionsToken expression) {
        super(parent);
        this.field = field;
        this.expression = expression;
    }

    public QuantaExpressionToken getField() {
        return field;
    }

    public ExpressionsToken getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return toExtendedString("field", field, "expression", expression);
    }

    public static class AssignmentParser implements Parser<AssignmentToken> {

        private static final Parser<?> parser = compound(
                quantaExpression,
                inline(either(word("="), word("<<"))),
                ExpressionsToken.ExpressionsParser.expression
        );
        public static AssignmentParser assignment = new AssignmentParser();

        @Override
        public AssignmentToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new AssignmentToken(
                    parent,
                    (QuantaExpressionToken) node.get(0),
                    (ExpressionsToken) node.get(2)
            );
        }
    }
}
