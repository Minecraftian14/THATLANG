package in.mcxiv.thatlang;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.LooseBlockParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;
import java.util.Objects;

import static in.mcxiv.thatlang.parser.power.PowerUtils.*;

public class ProgramFileToken extends Node {

    String programFileName;
    ProgramToken[] programs;
    FunctionToken[] functions;

    public ProgramFileToken(ProgramToken[] programs, FunctionToken[] functions) {
        this("loadedFromMemory", null, programs, functions);
    }

    public ProgramFileToken(String programFileName, ProgramToken[] programs, FunctionToken[] functions) {
        this(programFileName, null, programs, functions);
    }

    public ProgramFileToken(String programFileName, Node parent, ProgramToken[] programs, FunctionToken[] functions) {
        super(parent);
        this.programFileName = programFileName;
        this.programs = programs;
        this.functions = functions;
    }

    public ProgramToken[] getPrograms() {
        return programs;
    }

    public FunctionToken[] getFunctions() {
        return functions;
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

    public static class ProgramFileParser implements Parser<ProgramFileToken> {

        private static final LooseBlockParser parser = block(
                repeatable(block(either(
                        new ProgramToken.ProgramParser(),
                        FunctionToken.function
                )))
        );

        public static ProgramFileParser programFile = new ProgramFileParser();

        @Override
        public ProgramFileToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
//          if (node == null) return null;
            ProgramToken[] programTokens = node
                    .getChildren()
                    .stream()
                    .filter(ch -> ch instanceof ProgramToken)
                    .map(ch -> ((ProgramToken) ch)).toArray(ProgramToken[]::new);
            FunctionToken[] functionTokens = node
                    .getChildren()
                    .stream()
                    .filter(ch -> ch instanceof FunctionToken)
                    .map(ch -> ((FunctionToken) ch)).toArray(FunctionToken[]::new);
            return new ProgramFileToken(programTokens, functionTokens);
        }
    }

}
