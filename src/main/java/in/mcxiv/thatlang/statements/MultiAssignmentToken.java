package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.expression.MemberCallToken;
import in.mcxiv.thatlang.expression.QuantaExpressionToken;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import static in.mcxiv.thatlang.expression.QuantaExpressionToken.QuantaExpressionParser.quantaExpression;
import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.repeatable;
import static in.mcxiv.parser.generic.NameToken.NameParser.name;
import static in.mcxiv.parser.generic.SpacesToken.SpacesParser.spaces;

public class MultiAssignmentToken extends StatementToken {

    QuantaExpressionToken field;
    NameToken[] subFields;
    NameToken[] values;

    public MultiAssignmentToken(QuantaExpressionToken field, NameToken[] subFields, NameToken[] values) {
        this(null, field, subFields, values);
    }

    public MultiAssignmentToken(Node parent, QuantaExpressionToken field, NameToken[] subFields, NameToken[] values) {
        super(parent);
        this.field = field;
        this.subFields = subFields;
        this.values = values;
    }

    public QuantaExpressionToken getField() {
        return field;
    }

    public NameToken[] getSubFields() {
        return subFields;
    }

    public NameToken[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return toExtendedString("field", field, "subFields", subFields, "values", values);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        THATObject lhs = field.interpret(vm);
        for (int i = 0; i < subFields.length; i++) {
            THATObject value = THOSEObjects.createAfterReducing(values[i].getValue());
            lhs.putMember(subFields[i].getValue(), value);
        }
        return lhs;
    }

    public static class MultiAssignmentParser implements Parser<MultiAssignmentToken> {

        private static final Parser<?> parser = compound(
                quantaExpression,
                repeatable(compound(spaces, name))
        );
        public static MultiAssignmentParser multiAssignment = new MultiAssignmentParser();

        @Override
        public MultiAssignmentToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;

            var qet = (QuantaExpressionToken) node.get(0);

            if (qet.noOfChildren() < 2) return null;
            var children = qet.getChildren();
            Node qetElement = children.get(children.size() - 1);
            qetElement.detach();
            if (!(qetElement instanceof MemberCallToken)) return null;
            var member = ((MemberCallToken) qetElement);

            NameToken[] subFields = member.getValue().chars().mapToObj(value -> new NameToken("" + ((char) value))).toArray(NameToken[]::new);
            NameToken[] values = node.get(1).getChildren().stream().map(ch -> ch.get(1)).map(ch -> ((NameToken) ch)).toArray(NameToken[]::new);
            if (subFields.length != values.length) return null;

            return new MultiAssignmentToken(
                    parent,
                    qet,
                    subFields,
                    values
            );
        }
    }
}
