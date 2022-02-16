package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.OptionalParser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;

public class FunctionCallToken extends StringValueNode {

    ArgumentsToken arguments;

    public FunctionCallToken(String value, ArgumentsToken arguments) {
        this(null, value, arguments);
    }

    public FunctionCallToken(Node parent, String value, ArgumentsToken arguments) {
        super(parent, value);
        this.arguments = arguments;
        addChild(this.arguments);
    }

    @Override
    public String toString() {
        return toExtendedString("function name", getValue(), "arguments", arguments);
    }

    public ArgumentsToken getArguments() {
        return arguments;
    }

    public static class FunctionCallParser implements Parser<FunctionCallToken> {

        public static final FunctionCallParser instance = new FunctionCallParser();

        private static final Parser parser = new CompoundParser(
                NameToken.NameParser.instance,
                new LooseSpaceBoundedParser("("),
                new OptionalParser(new LooseSpaceBoundedParser(ArgumentsToken.ArgumentsParser.instance)),
                new LooseSpaceBoundedParser(")")
        );

        private FunctionCallParser() {
        }

        @Override
        public FunctionCallToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            String functionName = node.getExp(NameToken.class).getValue();
            ArgumentsToken arguments = node.getExp(ArgumentsToken.class);
            if(arguments == null) /*optional*/ arguments = new ArgumentsToken(new ExpressionsToken[0]);
            return new FunctionCallToken(parent, functionName, arguments);
        }
    }

}
