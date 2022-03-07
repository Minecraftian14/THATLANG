package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.thatlang.expression.MappedArgumentsToken.MappedArgumentsParser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.FunctionEvaluator;
import in.mcxiv.interpreter.Interpretable;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.*;

public class FunctionCallToken extends StringValueNode implements Interpretable<AbstractVM, THATObject> {

    MappedArgumentsToken arguments;

    public FunctionCallToken(String value, MappedArgumentsToken arguments) {
        this(null, value, arguments);
    }

    public FunctionCallToken(Node parent, String value, MappedArgumentsToken arguments) {
        super(parent, value);
        this.arguments = arguments;
        addChild(this.arguments);
    }

    @Override
    public String toString() {
        return toExtendedString("function name", getValue(), "arguments", arguments);
    }

    public MappedArgumentsToken getArguments() {
        return arguments;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        List<FunctionEvaluator> list = vm.executionEnvironment.getFunctionEvaluators();

        for (FunctionEvaluator evaluator : list)
            if (evaluator.isApplicable(this))
                return evaluator.apply(this);

        for (FunctionEvaluator evaluator : list)
            if (evaluator.isDigestible(this))
                return evaluator.apply(this);

        return THOSEObjects.NULL;
    }

    public static class FunctionCallParser implements Parser<FunctionCallToken> {

        public static final FunctionCallParser function = new FunctionCallParser();

        private static final Parser parser = compound(
                NameToken.NameParser.name,
                inline("("),
                optional(inline(MappedArgumentsParser.mappedArguments)),
                word(")")
        );

        private FunctionCallParser() {
        }

        @Override
        public FunctionCallToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            String functionName = node.getExp(NameToken.class).getValue();
            MappedArgumentsToken arguments = node.getExp(MappedArgumentsToken.class);
            if (arguments == null) /*optional*/ arguments = new MappedArgumentsToken(new MappedArgumentsToken.MappingsToken[0], new ExpressionsToken[0]);
            return new FunctionCallToken(parent, functionName, arguments);
        }
    }

}
