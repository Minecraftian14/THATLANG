package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken.ExpressionsParser;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.OptionalParser;
import in.mcxiv.thatlang.parser.power.WordParser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.NameToken.NameParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;

public class ForEachToken extends StatementToken {

    VariableDefinitionToken initializer;
    ExpressionsToken condition;
    AssignmentToken incremental;

    public ForEachToken(VariableDefinitionToken initializer, ExpressionsToken condition, AssignmentToken incremental, StatementToken[] statements) {
        this(null, initializer, condition, incremental, statements);
    }

    public ForEachToken(Node parent, VariableDefinitionToken initializer, ExpressionsToken condition, AssignmentToken incremental, StatementToken[] statements) {
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

    public static class ForEachParser implements Parser<ForEachToken> {

        public static final ForEachParser instance = new ForEachParser();

        private static final Parser<Node> parser = new CompoundParser(
                new WordParser("foreach"),
                new LooseSpaceBoundedParser(new OptionalParser(new CompoundParser(NameParser.instance, new WordParser("in")))),
                new LooseSpaceBoundedParser(ExpressionsParser.instance),
                BlockToken.BlockParser.instance
        );

        @Override
        public ForEachToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            String elementName = node.get(1).noOfChildren() > 0 ? ((NameToken) node.get(1).get(0)).getValue() : "that";
            var ini = node.getExp(VariableDefinitionToken.class);
            var con = node.getExp(ExpressionsToken.class);
            var inc = node.getExp(AssignmentToken.class);
            var bdy = node.getExp(BlockToken.class);
            return new ForEachToken(parent, ini, con, inc, bdy.getStatements());
        }
    }

}
