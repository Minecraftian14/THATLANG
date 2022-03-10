package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.parser.power.EitherParser;
import in.mcxiv.parser.power.LooseInlineParser;
import in.mcxiv.parser.power.WordParser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;

import static in.mcxiv.parser.power.PowerUtils.*;
import static thatlang.core.THOSEObjects.DATA_KEY_CONSTRUCTION_TYPE;

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

    @Override
    public THATObject interpret(AbstractVM vm) {
        THATObject rhs = expression.interpret(vm);
        rhs.putObjectData(DATA_KEY_CONSTRUCTION_TYPE, type);
        rhs.name = name;
        vm.getExecutionStack().peek().getB().addVariable(rhs);
        return rhs;
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
