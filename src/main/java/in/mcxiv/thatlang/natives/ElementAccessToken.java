package in.mcxiv.thatlang.natives;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.thatlang.expression.ExpressionsToken;
import in.mcxiv.thatlang.expression.FunctionCallToken;
import in.mcxiv.thatlang.expression.MappedArgumentsToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;

import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;

public class ElementAccessToken extends ExpressionsToken {

    NameToken field;
    List<FunctionCallToken> accessor;

    public ElementAccessToken(NameToken field, ExpressionsToken accessor) {
        this(null, field, accessor);
    }

    public ElementAccessToken(Node parent, NameToken field, ExpressionsToken accessor) {
        this(parent, field, List.of(accessor));
    }

    public ElementAccessToken(Node parent, NameToken field, List<ExpressionsToken> accessors) {
        super(parent);
        this.field = field;
        this.accessor = accessors.stream()
                .map(et -> new FunctionCallToken("__splice__", new MappedArgumentsToken(new MappedArgumentsToken.MappingsToken[0], new ExpressionsToken[]{et})))
                .toList();
    }

    @Override
    public String toString() {
        return toExtendedString("field", field, "accessor", accessor);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        THATObject object = vm.getExecutionStack().peek().getB().seek(field.getValue());
        assert object != null;
        return reduce(object);
    }

    public THATObject reduce(THATObject object) {
        for (var fct : accessor) object = object.seekFunction(fct);
        return object;
    }

    public NameToken getField() {
        return field;
    }

    public static class ElementAccessParser implements Parser<ElementAccessToken> {

        public static final Parser<ElementAccessToken> elementAccess = new ElementAccessParser();

        private static final Parser<?> parser = compound(
                NameToken.NameParser.name,
                repeatable(compound(inline("["), inline(ExpressionsParser.expression), word("]")))
        );

        private ElementAccessParser() {
        }

        @Override
        public ElementAccessToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;

            return new ElementAccessToken(
                    parent,
                    (NameToken) node.get(0),
                    node.get(1).getChildren()
                            .stream().map(n -> ((ExpressionsToken) n.get(1)))
                            .toList()
            );
        }
    }
}















