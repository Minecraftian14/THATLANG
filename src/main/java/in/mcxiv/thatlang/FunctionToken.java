package in.mcxiv.thatlang;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.generic.NameToken;
import in.mcxiv.parser.generic.NameToken.NameParser;
import in.mcxiv.parser.generic.StringValueNode;
import in.mcxiv.parser.generic.ValueToken;
import in.mcxiv.parser.generic.ValueToken.ValueParser;
import in.mcxiv.parser.power.TupleToken;
import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.blocks.BlockToken.BlockParser;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Cursors;

import java.util.List;

import static in.mcxiv.parser.power.PowerUtils.either;
import static in.mcxiv.parser.power.PowerUtils.word;

public class FunctionToken extends StringValueNode {

    String[] parameterNames;
    String[] returnArgNames;

    public FunctionToken(String name, String[] parameterNames, String[] returnArgNames, StatementToken[] statements) {
        this(null, name, returnArgNames, parameterNames, statements);
    }

    public FunctionToken(Node parent, String name, String[] parameterNames, String[] returnArgNames, StatementToken[] statements) {
        super(parent, name);
        this.parameterNames = parameterNames == null ? new String[0] : parameterNames;
        this.returnArgNames = returnArgNames == null ? new String[0] : returnArgNames;
        for (StatementToken statement : statements) addChild(statement);
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public String[] getReturnArgNames() {
        return returnArgNames;
    }

    public List<StatementToken> getStatements() {
        return getChildren(StatementToken.class);
    }

    @Override
    public String toString() {
        return toExtendedString("returnArgNames", returnArgNames, "parameterNames", parameterNames, "statements", getStatements());
    }

    public static final Parser<FunctionToken> function = new Parser<>() {

        private static final Parser<?>
                functionDefStart = either(word("function"), word("func"), word("fun"));

        private static final TupleToken.TupleParser<ValueToken, StringValueNode, TupleToken<ValueToken>>
                commaSeparatedNames = new TupleToken.TupleParser<>(ValueParser.value, word(","));

        @Override
        public FunctionToken __parse__(ParsableString string, Node parent) {

            // EXPECTED DEFINITION MAPPING FOR THE CODE BELOW
            // function           = function_def_start return_args_part name '(' parameters_part ')' block
            //
            // function_def_start = 'fun' | 'func' | 'function'
            // parameters_part    = comma_separated_names
            // return_args_part   = comma_separated_names

            if (functionDefStart.parse(string) == null) return null;
            Cursors.skipSpaces(string);

            TupleToken<ValueToken> csn = commaSeparatedNames.parse(string);
            String[] returnArgNames = csn == null ? new String[0] : csn.getItems().stream().map(ValueToken::getValue).toArray(String[]::new);
            Cursors.skipSpaces(string);

            var nameNode = NameParser.name.parse(string);
            if (nameNode == null) {
                // There's a chance that csn earlier caught away our function name
                if (returnArgNames.length != 1) return null;
                nameNode = new NameToken(returnArgNames[0]);
                returnArgNames = new String[0];
            }
            String name = nameNode.getValue();
            Cursors.skipSpaces(string);

            if (Cursors.getCharAndNext(string) != '(') return null;
            Cursors.skipSpaces(string);

            csn = commaSeparatedNames.parse(string);
            String[] parameterNames = csn == null ? new String[0] : csn.getItems().stream().map(ValueToken::getValue).toArray(String[]::new);
            Cursors.skipSpaces(string);

            if (Cursors.getCharAndNext(string) != ')') return null;
            Cursors.skipSpaces(string);

            BlockToken blockToken = BlockParser.block.parse(string);
            if (blockToken == null) return null;
            StatementToken[] statements = blockToken.getStatements();

            return new FunctionToken(parent, name, parameterNames, returnArgNames, statements);
        }
    };

}
