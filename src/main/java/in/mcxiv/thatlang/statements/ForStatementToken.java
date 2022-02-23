package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.AssignmentToken.AssignmentParser;
import in.mcxiv.thatlang.statements.VariableDefinitionToken.VariableDefinitionParser;

import java.util.List;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

public class ForStatementToken extends StatementToken {

    VariableDefinitionToken initializer;
    ExpressionsToken condition;
    AssignmentToken incremental;

    public ForStatementToken(VariableDefinitionToken initializer, ExpressionsToken condition, AssignmentToken incremental, StatementToken[] statements) {
        this(null, initializer, condition, incremental, statements);
    }

    public ForStatementToken(Node parent, VariableDefinitionToken initializer, ExpressionsToken condition, AssignmentToken incremental, StatementToken[] statements) {
        super(parent);
        this.initializer = initializer;
        this.condition = condition;
        this.incremental = incremental;
        for (Node statement : statements) addChild(statement);
    }

    public List<StatementToken> getStatements() {
        return getChildren(StatementToken.class);
    }

    public VariableDefinitionToken getInitializer() {
        return initializer;
    }

    public ExpressionsToken getCondition() {
        return condition;
    }

    public AssignmentToken getIncremental() {
        return incremental;
    }

    @Override
    public String toString() {
        return toExtendedString("initializer", initializer, "condition", condition, "incremental", incremental, "statements", getChildren());
    }

    public static class ForStatementParser implements Parser<ForStatementToken> {

        public static final ForStatementParser forStatement = new ForStatementParser();

        private static final Parser<Node> parser = compound(
                word("for"),
                inline("("),
                optional(either(VariableDefinitionParser.variableDef, AssignmentParser.assignment)),
                inline(";"),
                optional(ExpressionsParser.expression),
                inline(";"),
                optional(AssignmentParser.assignment),
                inline(")"),
                BlockToken.BlockParser.block
        );

        @Override
        public ForStatementToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            var ini = node.getExp(VariableDefinitionToken.class);
            var con = node.getExp(ExpressionsToken.class);
            var inc = node.getExp(AssignmentToken.class);
            var bdy = node.getExp(BlockToken.class);
            return new ForStatementToken(parent, ini, con, inc, bdy.getStatements());
        }
    }

}
