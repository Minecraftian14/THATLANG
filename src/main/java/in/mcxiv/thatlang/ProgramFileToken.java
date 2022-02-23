package in.mcxiv.thatlang;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.LooseBlockParser;
import in.mcxiv.thatlang.parser.tree.Node;

import java.util.List;
import java.util.Objects;

import static in.mcxiv.thatlang.parser.power.PowerUtils.repeatable;

public class ProgramFileToken extends Node {

    String programFileName;

    public ProgramFileToken(ProgramToken[] programs) {
        this("loadedFromMemory", null, programs);
    }

    public ProgramFileToken(String programFileName, ProgramToken[] programs) {
        this(programFileName, null, programs);
    }

    public ProgramFileToken(String programFileName, Node parent, ProgramToken[] programs) {
        super(parent);
        this.programFileName = programFileName;
        for (ProgramToken program : programs) addChild(program);
    }

    public List<ProgramToken> getProgramTokens() {
        return getChildren().stream()
                .filter(node -> node instanceof ProgramToken)
                .map(node -> ((ProgramToken) node))
                .toList();
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
        return toExtendedString("file name", getProgramFileName(), "programs", getChildren());
    }

    public static class ProgramFileParser implements Parser<ProgramFileToken> {

        private static final LooseBlockParser parser = new LooseBlockParser(
                repeatable(new LooseBlockParser(new ProgramToken.ProgramParser()))
        );

        public static ProgramFileParser programFile = new ProgramFileParser();

        @Override
        public ProgramFileToken __parse__(ParsableString string, Node parent) {
            Node parse = parser.parse(string);
//            if (parse == null) return null;
            ProgramToken[] programTokens = parse
                    .getChildren()
                    .stream()
//                    .map(node -> node.get(0)) // NEW
                    .map(node -> ((ProgramToken) node)).toArray(ProgramToken[]::new);
            return new ProgramFileToken(programTokens);
        }
    }

}
