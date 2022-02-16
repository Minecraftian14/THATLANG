package in.mcxiv.interp;

import in.mcxiv.thatlang.ProgramFileToken;
import in.mcxiv.thatlang.ProgramToken;
import in.mcxiv.thatlang.parser.expression.ExpressionsToken;
import in.mcxiv.thatlang.parser.expression.FunctionCallToken;
import in.mcxiv.thatlang.parser.expression.MemberCallToken;
import in.mcxiv.thatlang.parser.expression.QuantaExpressionToken;
import in.mcxiv.thatlang.statements.VariableDefinitionToken;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class InterpreterOLD {

    final Environment environment = /* Base Env */ new THATLANGEnvironment(null);

    final ArrayList<ProgramToken> programs = new ArrayList<>();

    final Stack<ProgramToken> executionStack = new Stack<>();

    ProgramFileToken mainFile;
    ProgramToken main;

    public InterpreterOLD() {
    }

    public InterpreterOLD(ProgramFileToken mainFile, String[] args) {
        this.mainFile = mainFile;
        this.main = getMain(this.mainFile);
        load(mainFile);
    }

    private ProgramToken getMain(ProgramFileToken file) {
        return file.getChildren().stream()
                .filter(node -> node instanceof ProgramToken)
                .map(node -> ((ProgramToken) node))
                .filter(program -> program.getProgramName().equals("main"))
                .findFirst().orElse((ProgramToken) this.mainFile.getChildren().get(0));
    }

    public void load(ProgramFileToken file) {
        List<ProgramToken> list = file.getChildren().stream()
                .filter(node -> node instanceof ProgramToken)
                .map(node -> ((ProgramToken) node))
                .filter(program -> !program.getProgramName().equals("main"))
                .toList();
        programs.addAll(list);
        list.stream().filter(program -> program.getProgramName().equals("init"))
                .forEach(this::run);
    }

    public void run() {
        run(main);
    }

    public void run(ProgramFileToken file) {
        run(getMain(file));
    }

    public void run(ProgramToken programToken) {
        executionStack.push(programToken);
        programToken.scope.clear();

        for (Node child : programToken.getChildren()) {
//            while (child.getClass().getName().equals(Node.class.getName()) && child.noOfChildren()==1)
//                child = child.get(0);
            if (child instanceof VariableDefinitionToken vdt)
                programToken.scope.push(new Pair<>(vdt.getName(), eval(vdt.getExpression())));
            else if (child instanceof QuantaExpressionToken qet) {
                if (qet.noOfChildren() == 1) {
                    if (qet.get(0) instanceof FunctionCallToken fct) {
                        if (fct.getArguments().noOfChildren() == 1)
                            System.out.println(eval(fct.getArguments().getExp(ExpressionsToken.class)));
                        else
                            System.out.println(fct.getArguments().getChildren().stream().map(node -> ((ExpressionsToken) node)).map(this::eval).toList());
                    }
                }

            }
        }
    }

    public String eval(ExpressionsToken expression) {
        String push = null;
        if (expression instanceof QuantaExpressionToken qet) {
            if (qet.noOfChildren() == 1) {
                if (qet.get(0) instanceof FunctionCallToken fct) {

                } else if (qet.get(0) instanceof MemberCallToken mct) {
//                    if(executionStack.peek().scope.)  check if there is a variable with that name
                    push = mct.getValue();
                }
            }
        }
        return push;
    }
}
