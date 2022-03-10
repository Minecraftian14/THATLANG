package in.mcxiv.thatlang;

import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.power.LooseInlineParser;
import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.interpreter.VariableScope;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Pair;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.word;

public class ContextToken extends Node implements Interpretable<AbstractVM, THATObject> {

    String contextName;

    public ContextToken(Node parent, String contextName, StatementToken[] nodes) {
        super(parent, nodes);
        this.contextName = contextName;
    }

    public String getContextName() {
        return contextName;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        vm.getExecutionStack().push(new Pair<>(null, new VariableScope()));
        try {
            getChildren(StatementToken.class).forEach(token -> token.interpret(vm));
        } catch (RuntimeException re) {
            if (!re.getMessage().equals(AbstractVM.EXECUTION_STOPPED))
                throw re;
        }
        VariableScope scope = vm.getExecutionStack().pop().getB();
        THATObject variable = THOSEObjects.createVariable(contextName, contextName);
        scope.foreach(variable::putMember);
        return variable;
    }

    public static final class ContextParser implements Parser<ContextToken> {

        public static final Parser<ContextToken> context = new ContextParser();

        private static final Parser<?> parser = compound(
                word("context"),
                new LooseInlineParser(NameToken.NameParser.name),
                BlockToken.BlockParser.block
        );

        private ContextParser() {
        }

        @Override
        public ContextToken __parse__(ParsableString string, Node parent) {
            Node compound = parser.parse(string);
            if (compound == null) return null;

            String contextName = compound.getExp(NameToken.class).getValue();
            StatementToken[] nodes = ((BlockToken) compound.get(2)).getStatements();

            return new ContextToken(parent, contextName, nodes);
        }
    }
}