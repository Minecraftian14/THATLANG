package in.mcxiv.thatlang;

import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.power.CompoundParser;
import in.mcxiv.thatlang.parser.power.LooseSpaceBoundedParser;
import in.mcxiv.thatlang.parser.power.OptionalParser;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.SpacesToken;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Pair;

import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class ProgramToken extends Node {

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
        return toExtendedString("program name" , programName, "statements" , getChildren());
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

    public static class ProgramParser implements Parser<ProgramToken> {

        private static final CompoundParser parser = new CompoundParser(
                NameToken.NameParser.instance,
                new LooseSpaceBoundedParser(""),
                new IndentedBlockToken.IndentedBlockParser()
        );

        @Override
        public ProgramToken __parse__(ParsableString string, Node parent) {
            Node compound = parser.parse(string);
            if (compound == null) return null;

            String programName = compound.getExp(NameToken.class).getValue();
            StatementToken[] nodes = ((IndentedBlockToken) compound.get(2)).getStatements();

            return new ProgramToken(parent, programName, nodes);
        }
    }

}
