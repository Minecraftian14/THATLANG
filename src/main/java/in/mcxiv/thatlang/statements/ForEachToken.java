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

    String elementName;
    ExpressionsToken condition;

    public ForEachToken(String elementName, ExpressionsToken con, StatementToken[] statements) {
        this(null, elementName, con, statements);
    }

    public ForEachToken(Node parent, String elementName, ExpressionsToken con, StatementToken[] statements) {
        super(parent);
        this.elementName = elementName;
        this.condition = con;
        for (Node statement : statements) addChild(statement);
    }

    public List<StatementToken> getStatements() {
        return getChildren(StatementToken.class);
    }

    public String getElementName() {
        return elementName;
    }

    public ExpressionsToken getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return toExtendedString("elementName", elementName, "condition", condition, "statements", getChildren());
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
            var con = node.getExp(ExpressionsToken.class);
            var bdy = node.getExp(BlockToken.class);
            return new ForEachToken(parent, elementName, con, bdy.getStatements());
        }
    }

}
