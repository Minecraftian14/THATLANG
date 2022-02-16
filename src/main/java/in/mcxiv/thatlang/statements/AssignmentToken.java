package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.WordParser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;

public class AssignmentToken extends StatementToken {

    String name;
    ExpressionsToken expression;

    public AssignmentToken(String name, ExpressionsToken expression) {
        this(null, name, expression);
    }

    public AssignmentToken(Node parent, String name, ExpressionsToken expression) {
        super(parent);
        this.name = name;
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public ExpressionsToken getExpression() {
        return expression;
    }

    public static class AssignmentParser implements Parser<AssignmentToken> {

        private static final Parser<?> parser = new CompoundParser(
                NameToken.NameParser.instance,
                new LooseSpaceBoundedParser(new EitherParser(new WordParser("="), new WordParser("<<"))),
                ExpressionsToken.ExpressionsParser.instance
        );
        public static AssignmentParser instance = new AssignmentParser();

        @Override
        public AssignmentToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new AssignmentToken(
                    parent,
                    node.getExp(NameToken.class).getValue(),
                    node.getExp(ExpressionsToken.class)
            );
        }
    }
}
