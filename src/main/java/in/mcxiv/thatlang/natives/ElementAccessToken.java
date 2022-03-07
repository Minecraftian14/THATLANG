package in.mcxiv.thatlang.natives;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.*;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;
import static in.mcxiv.thatlang.expression.SimpleSafeNonRecursiveExpressionParser.safeExpression;

public class ElementAccessToken extends ExpressionsToken {

    ExpressionsToken field;
    FunctionCallToken accessor;

    public ElementAccessToken(ExpressionsToken field, ExpressionsToken accessor) {
        this(null, field, accessor);
    }

    public ElementAccessToken(Node parent, ExpressionsToken field, ExpressionsToken accessor) {
        super(parent);
        this.field = field;
        this.accessor = new FunctionCallToken("__splice__", new MappedArgumentsToken(new MappedArgumentsToken.MappingsToken[0], new ExpressionsToken[]{accessor}));
    }

    @Override
    public String toString() {
        return toExtendedString("field", field, "accessor", accessor);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return field.interpret(vm).seekFunction(accessor);
    }

    public static class ElementAccessParser implements Parser<ElementAccessToken> {

        public static final Parser<ElementAccessToken> elementAccess = new ElementAccessParser();

        private static final Parser<?> parser = compound(safeExpression, inline("["), inline(ExpressionsParser.expression), word("]"));

        private ElementAccessParser() {
        }

        @Override
        public ElementAccessToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            return new ElementAccessToken(
                    parent,
                    (ExpressionsToken) node.get(0),
                    (ExpressionsToken) node.get(2)
            );
        }
    }
}















