package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.ValueToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.tryCatchSuite.Try;
import in.mcxiv.utils.Cursors;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import static in.mcxiv.parser.generic.NameToken.NameParser.name;
import static in.mcxiv.parser.power.PowerUtils.*;
import static in.mcxiv.thatlang.expression.FunctionCallToken.FunctionCallParser.function;
import static in.mcxiv.thatlang.expression.MemberCallToken.MemberCallParser.member;

public class MultiAssignmentToken extends ExpressionsToken {

    QuantaExpressionToken field;
    String[] subFields;
    String[] values;

    public MultiAssignmentToken(QuantaExpressionToken field, String[] subFields, String[] values) {
        this(null, field, subFields, values);
    }

    public MultiAssignmentToken(Node parent, QuantaExpressionToken field, String[] subFields, String[] values) {
        super(parent);
        this.field = field;
        this.subFields = subFields;
        this.values = values;
    }

    @Override
    public String toString() {
        return toExtendedString("field", field, "subFields", subFields, "values", values);
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        THATObject lhs = field.interpret(vm);
        for (int i = 0; i < subFields.length; i++) {
//          THATObject value = THOSEObjects.createAfterReducing(values[i]);
            // TODO: Refactor to store only QETs of length 1 so that we can directly call interpret
            THATObject member = lhs.getMember(subFields[i]);
            Object value = new QuantaExpressionToken(new Node[]{new MemberCallToken(values[i])}).interpret(vm).value;
            if (member == null) {
                lhs.putMember(subFields[i], THOSEObjects.createVariable(value));
            } else {
                THOSEObjects.mutateValue(member, value);
            }
        }
        return lhs;
    }

    public static class MultiAssignmentParser implements Parser<MultiAssignmentToken> {

        public static MultiAssignmentParser multiAssignment = new MultiAssignmentParser();

        private static final Parser<?> callStepParser = either(function, member);

        private static final Parser<?> fieldParser = compound(
                callStepParser,
                optional(repeatable(compound(word("."), callStepParser)))
        );

        @Override
        public MultiAssignmentToken __parse__(ParsableString string, Node parent) {
            Node node = fieldParser.parse(string);
            if (node == null) return null;

            var field = convert(node);

            if (!Cursors.bound(string) || !Cursors.isSpace(string)) return null;
            Cursors.skipSpaces(string);

            ArrayList<String[][]> pairs = new ArrayList<>();
            String[][] pair;
            while ((pair = parseSubfields(string)) != null) {
                pairs.add(pair);
                Cursors.skipSpaces(string);
            }
            if (pairs.size() < 1) return null;

            String[] subFields = pairs.stream().map(strings -> strings[0]).flatMap(Arrays::stream).toArray(String[]::new);
            String[] values = pairs.stream().map(strings -> strings[1]).flatMap(Arrays::stream).toArray(String[]::new);

            for (int i = 0; i < subFields.length; i++) {
                char c = subFields[i].charAt(0);
                int count = 0;
                for (int j = 0; j < i; j++)
                    if (subFields[j].charAt(0) == c)
                        count++;
                if (count > 0) subFields[i] += (count + 1);
            }

            if (subFields.length != values.length)
                throw new RuntimeException("Critical Error");

            return new MultiAssignmentToken(
                    parent,
                    field,
                    subFields,
                    values
            );
        }

        private QuantaExpressionToken convert(Node field) {
            ArrayList<Node> list = new ArrayList<>();
            list.add(field.get(0));
            IntStream.range(0, Try.GetAnd(() -> field.get(1).get(0).noOfChildren() / 2).Else(() -> 0))
                    .map(i -> 2 * i + 1)
                    .mapToObj(index -> field.get(1).get(0).get(index))
                    .forEach(list::add);
            return new QuantaExpressionToken(list.toArray(new Node[0]));
        }

        private String[][] parseSubfields(ParsableString string) {
            if (Cursors.getChar(string) != '.') return null;
            string.moveCursor(1);
            NameToken node = name.parse(string);
            if (node == null) return null;

            String[] subFields = node.getValue().chars().mapToObj(value -> "" + (char) value).toArray(String[]::new);
            String[] values = new String[subFields.length];

            for (int i = 0; i < subFields.length; i++) {
                Cursors.skipSpaces(string);
                ValueToken vt = ValueToken.ValueParser.value.parse(string);
                if (vt == null) return null;
                values[i] = vt.getValue();
            }
            return new String[][]{subFields, values};
        }
    }
}
