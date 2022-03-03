package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.QuantaExpressionToken;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import static in.mcxiv.thatlang.expression.QuantaExpressionToken.QuantaExpressionParser.quantaExpression;
import static in.mcxiv.parser.power.PowerUtils.*;

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

    @Override
    public THATObject interpret(AbstractVM vm) {
        THATObject lhs = field.interpret(vm);
        THOSEObjects.mutateValue(lhs, expression.interpret(vm).value);
        return lhs;
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
