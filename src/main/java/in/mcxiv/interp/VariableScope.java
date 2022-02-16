package in.mcxiv.interp;

import in.mcxiv.thatlang.parser.expression.FunctionCallToken;

import java.util.ArrayList;

public class VariableScope {

    ArrayList<Variable> variables;

    public VariableScope() {
        variables = new ArrayList<>();
    }

    public void newVariable(String type, String name, String eval) {
        variables.add(new Variable(type, name, eval));
    }

    public Variable seek(String name) {
        return variables.stream().filter(variable -> variable.name.equals(name)).findFirst().orElse(null);
    }

    public static class Variable {

        public static final Variable NULL = new Variable("val", "null", "null");
        String type, name, value;

        public Variable(String type, String name, String value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public void seekMember(String name) {

        }

        public static Variable of(String value) {
            return new Variable("val", "null", value);
        }

        public void seekFunction(FunctionCallToken function) {

        }

        public String  v() {
            return value;
        }
    }

}
