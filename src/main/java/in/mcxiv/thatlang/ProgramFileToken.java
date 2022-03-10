package in.mcxiv.thatlang;

import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.LooseBlockParser;
import in.mcxiv.thatlang.ContextToken.ContextParser;
import in.mcxiv.thatlang.interpreter.AbstractVM;

import java.util.Arrays;
import java.util.Objects;

import static in.mcxiv.parser.power.PowerUtils.*;

public class ProgramFileToken extends Node implements Interpretable<AbstractVM, ProgramFileToken> {

    String programFileName;
    ProgramToken[] programs;
    FunctionToken[] functions;
    ContextToken[] contexts;

    public ProgramFileToken(ProgramToken[] programs, FunctionToken[] functions, ContextToken[] contexts) {
        this("loadedFromMemory", null, programs, functions, contexts);
    }

    public ProgramFileToken(String programFileName, ProgramToken[] programs, FunctionToken[] functions, ContextToken[] contexts) {
        this(programFileName, null, programs, functions, contexts);
    }

    public ProgramFileToken(String programFileName, Node parent, ProgramToken[] programs, FunctionToken[] functions, ContextToken[] contexts) {
        super(parent);
        this.programFileName = programFileName;
        this.programs = programs;
        this.functions = functions;
        this.contexts = contexts;
    }

    public ProgramToken[] getPrograms() {
        return programs;
    }

    public FunctionToken[] getFunctions() {
        return functions;
    }

    public ContextToken[] getContexts() {
        return contexts;
    }

    public void setProgramFileName(String programFileName) {
        this.programFileName = Objects.requireNonNull(programFileName);
    }

    public String getProgramFileName() {
        return programFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProgramFileToken that = (ProgramFileToken) o;
        return getProgramFileName().equals(that.getProgramFileName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProgramFileName());
    }

    @Override
    public String toString() {
        return toExtendedString("file name", getProgramFileName(), "programs", getPrograms(), "functions", getFunctions());
    }

    public ProgramToken getMain() {
        return Arrays.stream(getPrograms())
                .filter(program -> program.getProgramName().equals("main"))
                .findFirst().orElse(getPrograms()[0]);
    }

    @Override
    public ProgramFileToken interpret(AbstractVM vm) {
        vm.run(getMain());
        return this;
    }

    public static class ProgramFileParser implements Parser<ProgramFileToken> {

        private static final LooseBlockParser parser = block(
                repeatable(block(either(
                        new ProgramToken.ProgramParser(),
                        FunctionToken.function,
                        ContextParser.context
                )))
        );

        public static ProgramFileParser programFile = new ProgramFileParser();

        @Override
        public ProgramFileToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
//          if (node == null) return null;
            ProgramToken[] programTokens = node.getChildren(ProgramToken.class).toArray(ProgramToken[]::new);
            FunctionToken[] functionTokens = node.getChildren(FunctionToken.class).toArray(FunctionToken[]::new);
            ContextToken[] contextTokens = node.getChildren(ContextToken.class).toArray(ContextToken[]::new);
            return new ProgramFileToken(programTokens, functionTokens, contextTokens);
        }
    }

}
