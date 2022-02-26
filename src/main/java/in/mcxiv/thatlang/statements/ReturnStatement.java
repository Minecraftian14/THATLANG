package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.tree.Node;

import static in.mcxiv.thatlang.parser.power.PowerUtils.compound;
import static in.mcxiv.thatlang.parser.power.PowerUtils.word;
import static in.mcxiv.thatlang.parser.tokens.SpacesToken.SpacesParser.spaces;

public class ReturnStatement extends Node {

    ExpressionsToken expression;

    public ReturnStatement(ExpressionsToken expression) {
        this(null, expression);
    }

    public ReturnStatement(Node parent, ExpressionsToken expression) {
        super(parent);
        this.expression = expression;
    }

    public ExpressionsToken getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return toExtendedString("returns", expression);
    }

    public static final Parser<ReturnStatement> returnStmt = new Parser<ReturnStatement>() {

        private static final Parser<Node> parser = compound(word("return"), spaces, ExpressionsParser.expression);

        @Override
        public ReturnStatement __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new ReturnStatement(node.getExp(ExpressionsToken.class));
        }
    };
}
