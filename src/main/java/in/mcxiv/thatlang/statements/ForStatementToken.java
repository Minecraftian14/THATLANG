package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.IndentedBlockToken;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.WordParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;

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

        public static final ForStatementParser instance = new ForStatementParser();

        private static final Parser<Node> parser = new CompoundParser(
                new WordParser("for"),
                new LooseSpaceBoundedParser("("),
                VariableDefinitionToken.VariableDefinitionParser.instance,
                new LooseSpaceBoundedParser(";"),
                ExpressionsToken.ExpressionsParser.instance,
                new LooseSpaceBoundedParser(";"),
                AssignmentToken.AssignmentParser.instance,
                new LooseSpaceBoundedParser(")"),
                new LooseSpaceBoundedParser("->"),
                IndentedBlockToken.IndentedBlockParser.instance
        );

        @Override
        public ForStatementToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            var ini = node.getExp(VariableDefinitionToken.class);
            var con = node.getExp(ExpressionsToken.class);
            var inc = node.getExp(AssignmentToken.class);
            var bdy = node.getExp(IndentedBlockToken.class);
            return new ForStatementToken(parent, ini, con, inc, bdy.getStatements());
        }
    }

}
