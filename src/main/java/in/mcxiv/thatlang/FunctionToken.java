package in.mcxiv.thatlang;

import in.mcxiv.thatlang.blocks.BlockToken;
import in.mcxiv.thatlang.blocks.BlockToken.BlockParser;
import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.natives.TupleToken;
import in.mcxiv.thatlang.parser.tokens.NameToken;
import in.mcxiv.thatlang.parser.tokens.NameToken.NameParser;
import in.mcxiv.thatlang.parser.tokens.generic.StringValueNode;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.StatementToken;
import in.mcxiv.utils.Cursors;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.thatlang.parser.power.PowerUtils.either;
import static in.mcxiv.thatlang.parser.power.PowerUtils.word;

public class FunctionToken extends StringValueNode {

    String[] parameterNames;

    public FunctionToken(String name, String[] parameterNames, StatementToken[] statements) {
        this(null, name, parameterNames, statements);
    }

    public FunctionToken(Node parent, String name, String[] parameterNames, StatementToken[] statements) {
        super(parent, name);
        this.parameterNames = parameterNames == null ? new String[0] : parameterNames;
        for (StatementToken statement : statements) addChild(statement);
    }

    public String[] getParameterNames() {
        return parameterNames;
    }

    public List<StatementToken> getStatements() {
        return getChildren(StatementToken.class);
    }

    public static final Parser<FunctionToken> function = new Parser<>() {

        private static final Parser<?>
                functionDefStart = either(word("function"), word("func"), word("fun"));

        private static final TupleToken.TupleParser<NameToken, StringValueNode, TupleToken<NameToken>>
                parameters = new TupleToken.TupleParser<>(NameParser.name, word(","));

        @Override
        public FunctionToken __parse__(ParsableString string, Node parent) {
            if (functionDefStart.parse(string) == null) return null;
            Cursors.skipSpaces(string);

            NameToken nameToken = NameParser.name.parse(string);
            if (nameToken == null) return null;
            String name = nameToken.getValue();
            Cursors.skipSpaces(string);

            String[] parameterNames = new String[0];
            if (Cursors.getChar(string) == '<') {
                string.moveCursor(1);
                Cursors.skipSpaces(string);

                TupleToken<NameToken> parametersToken = parameters.parse(string);
                if (parametersToken == null) return null;
                ArrayList<NameToken> items = parametersToken.getItems();
                parameterNames = new String[items.size()];
                for (int i = 0; i < parameterNames.length; i++)
                    parameterNames[i] = items.get(i).getValue();
            }
            Cursors.skipSpaces(string);

            BlockToken blockToken = BlockParser.block.parse(string);
            if (blockToken == null) return null;
            StatementToken[] statements = blockToken.getStatements();

            return new FunctionToken(parent, name, parameterNames, statements);
        }
    };

}
