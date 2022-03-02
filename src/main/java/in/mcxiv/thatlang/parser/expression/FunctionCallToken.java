package in.mcxiv.thatlang.parser.expression;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.ArrayList;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

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

        public static final FunctionCallParser function = new FunctionCallParser();

        private static final Parser parser = compound(
                NameToken.NameParser.name,
                inline("("),
                optional(inline(ArgumentsToken.arguments)),
                inline(")")
        );

        private FunctionCallParser() {
        }

        @Override
        public FunctionCallToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            String functionName = node.getExp(NameToken.class).getValue();
            ArgumentsToken arguments = node.getExp(ArgumentsToken.class);
            if(arguments == null) /*optional*/ arguments = new ArgumentsToken();
            return new FunctionCallToken(parent, functionName, arguments);
        }
    }

}
