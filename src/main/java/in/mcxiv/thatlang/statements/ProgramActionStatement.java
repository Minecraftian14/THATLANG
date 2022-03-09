package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.NameToken.NameParser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.ArrayList;

import static in.mcxiv.parser.power.PowerUtils.*;

public class ProgramActionStatement extends StatementToken {

    /**
     * Pauses the current program.
     * Starts executing another program.
     * When that's over, resumes the initial program.
     */
    public static final String PROGRAM_START = "start";

    /**
     * Stops the current program.
     * Starts executing another program.
     */
    public static final String PROGRAM_END_WITH = "end with";

    /**
     * Starts executing another program in a separate Thread.
     */
    public static final String PROGRAM_LAUNCH = "launch";

    /**
     * Returns the named program as a THATObject
     */
    public static final String PROGRAM_DEMAND = "demand";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private final String action;
    private final String name;
    private final ArrayList<String> constraints;

    public ProgramActionStatement(Node parent, String action, String name, ArrayList<String> constraints) {
        super(parent);
        this.action = action;
        this.name = name;
        this.constraints = constraints;
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        THATObject returns = THOSEObjects.NULL;
        ProgramToken program = vm.executionEnvironment.getPrograms().stream().filter(pt -> name.equals(pt.getProgramName())).findFirst().orElseThrow(() -> new RuntimeException("No such program called %s defined".formatted(name)));
        switch (action) {
            case PROGRAM_START -> vm.run(program);
            case PROGRAM_END_WITH -> {
                vm.run(program);
                vm.__STOP_THIS__();
            }
            case PROGRAM_LAUNCH -> vm.launch(program);
            case PROGRAM_DEMAND -> returns = THOSEObjects.createValue(program);
        }
        return returns;
    }

    // TODO: Add another node at program/function level called context, which stores variables, usable/passable among programs.


    public static final class ProgramActionParser implements Parser<ProgramActionStatement> {

        public static final Parser<ProgramActionStatement> programAction = new ProgramActionParser();

        private static final TupleToken.TupleParser<NameToken, StringValueNode, TupleToken<NameToken>>
                constraintsParser = new TupleToken.TupleParser<>(NameParser.name, word(","));
        private static final Parser<?> parser = compound(
                either(word(PROGRAM_START), word(PROGRAM_END_WITH), word(PROGRAM_LAUNCH), word(PROGRAM_DEMAND)),
                inline(NameParser.name) /*prog name*/,
                optional(compound(word("with"), constraintsParser))
        );

        private ProgramActionParser() {
        }

        @Override
        public ProgramActionStatement __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string);
            if (node == null) return null;
            String action = ((StringValueNode) node.get(0)).getValue();
            String name = ((StringValueNode) node.get(1)).getValue();
            node = node.get(2);
            ArrayList<String> constraints = new ArrayList<>();
            if (node.noOfChildren() > 0)
                constraints.addAll(constraintsParser.cast(node.get(1)).getItems().stream().map(StringValueNode::getValue).toList());
            return new ProgramActionStatement(parent, action, name, constraints);
        }
    }
}
