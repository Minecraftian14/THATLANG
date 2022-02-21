package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.OptionalParser;
import in.mcxiv.thatlang.parser.power.RepeatableParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ArgumentsToken extends Node {

    public ArgumentsToken(ExpressionsToken[] expressions) {
        this(null, expressions);
    }

    public ArgumentsToken(Node parent, ExpressionsToken[] expressions) {
        super(parent);
        for (ExpressionsToken expression : expressions)
            addChild(expression);
    }

    @Override
    public String toString() {
        return toExtendedString("arguments", getChildren());
    }

    public List<ExpressionsToken> getExpressions() {
        return getChildren(ExpressionsToken.class);
    }

    public static class ArgumentsParser implements Parser<ArgumentsToken> {

        public static final ArgumentsParser instance = new ArgumentsParser();

        private static final Parser parser = new CompoundParser(
                ExpressionsToken.ExpressionsParser.instance,
                new OptionalParser(new RepeatableParser(new LooseSpaceBoundedParser(","), ExpressionsToken.ExpressionsParser.instance))
        );

        private ArgumentsParser() {
        }

        @Override
        public ArgumentsToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            ArrayList<ExpressionsToken> expressions = new ArrayList<>();
            expressions.add(node.getExp(ExpressionsToken.class));
            if (node.noOfChildren() > 1)
                node.get(1).getChildren().stream()
                        .map(ch -> ch.get(1))
                        .map(ExpressionsToken.class::cast)
                        .forEach(expressions::add);
//            IntStream.range(0, node.get(1).noOfChildren())
//                    .map(i -> 2 * i)
//                    .mapToObj(i -> node.get(1).get(1))
//                    .map(ExpressionsToken.class::cast)
//                    .forEach(expressions::add);
            return new ArgumentsToken(parent, expressions.toArray(new ExpressionsToken[0]));
        }
    }
}