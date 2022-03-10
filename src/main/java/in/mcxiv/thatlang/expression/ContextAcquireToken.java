package in.mcxiv.thatlang.expression;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.NameToken.NameParser;
import in.mcxiv.parser.generic.SpacesToken.SpacesParser;
import in.mcxiv.thatlang.ContextToken;
import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;

import static in.mcxiv.parser.power.PowerUtils.*;

public class ContextAcquireToken extends ExpressionsToken {

    private final String name;

    public ContextAcquireToken(Node parent, String value) {
        super(parent);
        this.name = value;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        for (ProgramFileToken programFile : vm.executionEnvironment.getProgramFiles())
            for (ContextToken context : programFile.getContexts())
                if (name.equals(context.getContextName()))
                    return context.interpret(vm);
        throw new IllegalStateException("No context named %s defined!");
    }

    public static final class ContextAcquireParser implements Parser<ContextAcquireToken> {

        public static final Parser<ContextAcquireToken> contextAcquire = new ContextAcquireParser();

        private static final Parser<?> parser = compound(word("acquire"), SpacesParser.spaces, NameParser.name);

        private ContextAcquireParser() {
        }

        @Override
        public ContextAcquireToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            return node == null ? null : new ContextAcquireToken(parent, ((NameToken) node.get(2)).getValue());
        }
    }
}
