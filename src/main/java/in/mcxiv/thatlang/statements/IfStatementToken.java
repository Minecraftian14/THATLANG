package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

public class IfStatementToken extends StatementToken {

    ExpressionsToken condition;
    ArrayList<ElseIfStatementToken> elseIfSts = new ArrayList<>();
    ElseStatementToken elseSt = null;

    public IfStatementToken(ExpressionsToken condition, StatementToken[] statements) {
        this(null, condition, statements);
    }

    public IfStatementToken(Node parent, ExpressionsToken condition, StatementToken[] statements) {
        super(parent, ElseStatementToken.class, ElseIfStatementToken.class);
        this.condition = condition;
        for (Node statement : statements) addChild(statement);
    }

    @Override
    public boolean isAccepted(StatementToken token) {
        if (token instanceof ElseStatementToken && elseSt != null)
            return false;
        return super.isAccepted(token);
    }

    @Override
    public void processCondensability(StatementToken token) {
        if (token instanceof ElseStatementToken est)
            elseSt = est;
        else if (token instanceof ElseIfStatementToken eist)
            elseIfSts.add(eist);
    }

    public List<StatementToken> getStatements() {
        return getChildren(StatementToken.class);
    }

    public ExpressionsToken getCondition() {
        return condition;
    }

    public ArrayList<ElseIfStatementToken> getElseIfSts() {
        return elseIfSts;
    }

    public ElseStatementToken getElseSt() {
        return elseSt;
    }

    @Override
    public String toString() {
        return toExtendedString("condition", condition, "statements", getChildren());
    }

    public static class IfStatementParser implements Parser<IfStatementToken> {

        public static final IfStatementParser ifStatement = new IfStatementParser();

        private static final Parser<Node> parser = either(
                compound(
                        word("if"),
                        inline("("),
                        ExpressionsParser.expression,
                        inline(")"),
                        BlockToken.BlockParser.block
                ),
                compound(
                        word("if"),
                        inline(ExpressionsParser.expression),
                        BlockToken.BlockParser.block
                )
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
