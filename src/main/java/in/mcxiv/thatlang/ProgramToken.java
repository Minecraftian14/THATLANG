package in.mcxiv.thatlang;

import in.mcxiv.thatlang.interpreter.VariableScope;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.power.CompoundParser;
import in.mcxiv.parser.power.LooseInlineParser;
import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Pair;
import interpreter.Interpretable;

import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

import static in.mcxiv.parser.power.PowerUtils.compound;
import static in.mcxiv.parser.power.PowerUtils.word;

public class ProgramToken extends Node implements Interpretable<AbstractVM, ProgramToken> {

    // To be used by interpreter
    public Stack<Pair<String, String>> scope = new Stack<>();

    String programName;

    public ProgramToken(String programName, StatementToken[] statements) {
        this(null, programName, statements);
    }

    public ProgramToken(Node parent, String programName, StatementToken[] statements) {
        super(parent);
        this.programName = programName;
        for (Node statement : statements) addChild(statement);
    }

    public String getProgramName() {
        return programName;
    }

    public List<StatementToken> getStatements() {
        return getChildren().stream().filter(node -> node instanceof StatementToken)
                .map(node -> ((StatementToken) node))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return toExtendedString("program name", programName, "statements", getChildren());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramToken that = (ProgramToken) o;
        return getProgramName().equals(that.getProgramName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProgramName());
    }

    @Override
    public ProgramToken interpret(AbstractVM vm) {
        vm.executionStack.push(new Pair<>(this, new VariableScope()));
        getStatements().forEach(token -> token.interpret(vm));
        vm.executionStack.pop();
        return this;
    }

    public static class ProgramParser implements Parser<ProgramToken> {

        private static final CompoundParser parser = compound(
                word("program"),
                new LooseInlineParser(NameToken.NameParser.name),
                BlockToken.BlockParser.block
        );

        @Override
        public ProgramToken __parse__(ParsableString string, Node parent) {
            Node compound = parser.parse(string);
            if (compound == null) return null;

            String programName = compound.getExp(NameToken.class).getValue();
            StatementToken[] nodes = ((BlockToken) compound.get(2)).getStatements();

            return new ProgramToken(parent, programName, nodes);
        }
    }

}
