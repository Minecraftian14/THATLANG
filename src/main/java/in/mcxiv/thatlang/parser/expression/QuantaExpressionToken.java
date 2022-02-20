package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.*;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.tryCatchSuite.Try;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.IntStream;

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

    public static class QuantaExpressionParser implements Parser<QuantaExpressionToken> {

        public static final QuantaExpressionParser instance = new QuantaExpressionParser();

        private static final Parser callStepParser = new EitherParser(
                StringToken.StringParser.instance,
                FunctionCallToken.FunctionCallParser.instance,
                MemberCallToken.MemberCallParser.instance // Will also catch 1234 :cry:... // TODO: Not a good thing either, because it's catching every possible Name afterwards
        );

        private static final Parser parser = new CompoundParser(
                callStepParser,
                new OptionalParser(new RepeatableParser(new WordParser("."), callStepParser))
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
