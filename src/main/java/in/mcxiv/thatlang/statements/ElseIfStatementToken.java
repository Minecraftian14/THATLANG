package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;
import static in.mcxiv.thatlang.parser.tokens.SpacesToken.SpacesParser.spaces;
import static in.mcxiv.thatlang.statements.IfStatementToken.IfStatementParser.ifStatement;

public class ElseIfStatementToken extends StatementToken {

    ExpressionsToken condition;

    public ElseIfStatementToken(ExpressionsToken condition, StatementToken[] statements) {
        this(null, condition, statements);
    }

    public ElseIfStatementToken(Node parent, ExpressionsToken condition, StatementToken[] statements) {
        super(parent);
        this.condition = condition;
        for (Node statement : statements) addChild(statement);
    }

    public List<StatementToken> getStatements() {
        return getChildren(StatementToken.class);
    }

    public ExpressionsToken getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return toExtendedString("condition", condition, "statements", getChildren());
    }

    public static class ElseIfStatementParser implements Parser<ElseIfStatementToken> {

        public static final ElseIfStatementParser elseIfStatement = new ElseIfStatementParser();

        private static final Parser<Node> parser = compound(
                either(compound(word("else"), spaces), word("el")),
                ifStatement
        );

        @Override
        public ElseIfStatementToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            var is = node.getExp(IfStatementToken.class);
            return new ElseIfStatementToken(parent, is.getCondition(), is.getStatements().toArray(new StatementToken[0]));
        }
    }
}
