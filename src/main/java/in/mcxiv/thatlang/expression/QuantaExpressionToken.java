package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.power.EitherParser;
import in.mcxiv.parser.power.WordParser;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.natives.StringToken;
import in.mcxiv.parser.Node;
import in.mcxiv.tryCatchSuite.Try;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

import static in.mcxiv.parser.power.PowerUtils.*;

public class QuantaExpressionToken extends ExpressionsToken implements Iterable<Node> {

    public QuantaExpressionToken(Node[] tokens) {
        this(null, tokens);
    }

    public QuantaExpressionToken(Node parent, Node[] tokens) {
        super(parent);
        for (Node token : tokens) addChild(token);
    }

    @Override
    public Iterator<Node> iterator() {
        return new QuantaExpressionIterator(this);
    }

    public QuantaExpressionIterator quantaIterator() {
        return new QuantaExpressionIterator(this);
    }

    @Override
    public String toString() {
        return toExtendedString("call seq", getChildren());
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        var iterator = quantaIterator();

//        executionStack.peek().getB().
        THATObject variable = null;

        while (iterator.hasNext()) {

            if (iterator.isString()) {
                String value = iterator.nextString().getValue();
                assert variable == null : "Cant access a string as if it's a member of some variable...";
                variable = THOSEObjects.createValue(value);

            } else if (iterator.isMember()) {
                String value = iterator.nextMember().getValue();
                if (variable == null) /* ie we're probably accessing a local field */ {
                    variable = vm.executionStack.peek().getB().seek(value);
                    if (variable == null) /* ie there is no such variable created by that name */
                        variable = THOSEObjects.createAfterReducing(value); // so we treat it as if it's a value
                } else /* ie we're accessing a field in variable */ {
                    var member = variable.getMember(value);
                    if (member == null) /* ie we are probably creating something? */ {
                        member = THOSEObjects.createValue(null);
                        variable.putMember(value, member);
                    }
                    variable = member;
                }

            } else if (iterator.isFunction()) {
                FunctionCallToken function = iterator.nextFunction();
                if (variable == null)
                    variable = function.interpret(vm);
                else
                    variable = variable.seekFunction(function);
            }
        }
        return variable == null ? THOSEObjects.NULL : variable;
    }

    public static class QuantaExpressionParser implements Parser<QuantaExpressionToken> {

        public static final QuantaExpressionParser quantaExpression = new QuantaExpressionParser();

        private static final Parser callStepParser = new EitherParser(
                FunctionCallToken.FunctionCallParser.function,
                MemberCallToken.MemberCallParser.member // Will also catch 1234
        );

        private static final Parser parser = compound(
                callStepParser,
                optional(repeatable(compound(new WordParser("."), callStepParser)))
        );

        private QuantaExpressionParser() {
        }

        @Override
        public QuantaExpressionToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            ArrayList<Node> list = new ArrayList<>();
            list.add(node.get(0));
            IntStream.range(0, Try.GetAnd(() -> node.get(1).get(0).noOfChildren() / 2).Else(() -> 0))
                    .map(i -> 2 * i + 1)
                    .mapToObj(index -> node.get(1).get(0).get(index))
                    .forEach(list::add);
            return new QuantaExpressionToken(parent, list.toArray(new Node[0]));
        }
    }

    public static final class QuantaExpressionIterator implements Iterator<Node> {

        QuantaExpressionToken token;
        Node[] tokens;
        int index = 0;

        public QuantaExpressionIterator(QuantaExpressionToken token) {
            this.token = token;
            this.tokens = token.getChildren().toArray(new Node[0]);
        }

        @Override
        public boolean hasNext() {
            return index < tokens.length;
        }

        @Override
        public Node next() {
            return tokens[index++];
        }

        public boolean isString() {
            if (hasNext()) return tokens[index] instanceof StringToken;
            return false;
        }

        public boolean isMember() {
            if (hasNext()) return tokens[index] instanceof MemberCallToken;
            return false;
        }

        public boolean isFunction() {
            if (hasNext()) return tokens[index] instanceof FunctionCallToken;
            return false;
        }

        public StringToken nextString() {
            return (StringToken) next();
        }

        public MemberCallToken nextMember() {
            return (MemberCallToken) next();
        }

        public FunctionCallToken nextFunction() {
            return (FunctionCallToken) next();
        }

    }

}
