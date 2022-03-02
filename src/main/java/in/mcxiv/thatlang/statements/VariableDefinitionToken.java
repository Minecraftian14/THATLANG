package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.power.LooseInlineParser;
import in.mcxiv.thatlang.parser.power.WordParser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

public class VariableDefinitionToken extends StatementToken {

    String type; // val or var
    String name;
    ExpressionsToken expression;

    public VariableDefinitionToken(String type, String name, ExpressionsToken expression) {
        this(null, type, name, expression);
    }

    public VariableDefinitionToken(Node parent, String type, String name, ExpressionsToken expression) {
        super(parent);
        this.type = type;
        this.name = name;
        this.expression = expression;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ExpressionsToken getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return toExtendedString("type", type, "name", name, "expression", expression);
    }

    public static class VariableDefinitionParser implements Parser<VariableDefinitionToken> {

        private static final Parser<?> parser = compound(
                either(word("var"), new WordParser("val")),
                new LooseInlineParser(NameToken.NameParser.name),
                new LooseInlineParser(new EitherParser(new WordParser("="), new WordParser("<<"))),
                ExpressionsToken.ExpressionsParser.expression
        );
        public static VariableDefinitionParser variableDef = new VariableDefinitionParser();

        @Override
        public VariableDefinitionToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new VariableDefinitionToken(
                    parent,
                    ((StringValueNode) node.get(0)).getValue(),
                    ((NameToken) node.get(1)).getValue(),
                    node.getExp(ExpressionsToken.class)
            );
        }
    }
}
