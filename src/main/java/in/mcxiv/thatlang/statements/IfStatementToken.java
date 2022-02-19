package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.WordParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;

public class IfStatementToken extends StatementToken {

    ExpressionsToken condition;

    public IfStatementToken(ExpressionsToken condition, StatementToken[] statements) {
        this(null, condition, statements);
    }

    public IfStatementToken(Node parent, ExpressionsToken condition, StatementToken[] statements) {
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

    public static class IfStatementParser implements Parser<IfStatementToken> {

        public static final IfStatementParser instance = new IfStatementParser();

        private static final Parser<Node> parser = new CompoundParser(
                new WordParser("if"),
                new LooseSpaceBoundedParser("("),
                ExpressionsParser.instance,
                new LooseSpaceBoundedParser(")"),
                BlockToken.BlockParser.instance
        );

        @Override
        public IfStatementToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            var con = node.getExp(ExpressionsToken.class);
            var bdy = node.getExp(BlockToken.class);
            return new IfStatementToken(parent, con, bdy.getStatements());
        }
    }

}
