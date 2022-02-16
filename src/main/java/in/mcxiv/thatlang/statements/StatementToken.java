package in.mcxiv.thatlang.statements;

import in.mcxiv.thatlang.parser.ParsableString;
import in.mcxiv.thatlang.parser.Parser;
import in.mcxiv.thatlang.parser.power.EitherParser;
import in.mcxiv.thatlang.parser.tree.Node;
import in.mcxiv.thatlang.statements.AssignmentToken.AssignmentParser;
import in.mcxiv.thatlang.statements.ForStatementToken.ForStatementParser;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.thatlang.statements.QuantaStatement.QuantaStatementParser;
import static in.mcxiv.thatlang.statements.VariableDefinitionToken.VariableDefinitionParser;

public class StatementToken extends Node {

    public static final ArrayList<Parser<?>> STATEMENT_PARSERS = new ArrayList<>(List.of(
            VariableDefinitionParser.instance,
            AssignmentParser.instance,
            ForStatementParser.instance,
            QuantaStatementParser.instance
    ));

    public static final ArrayList<Class<?>> STATEMENT_TYPES = new ArrayList<>(List.of(
            VariableDefinitionToken.class,
            AssignmentToken.class,
            ForStatementToken.class,
            QuantaStatement.class
    ));

    public StatementToken() {
    }

    public StatementToken(Node parent) {
        super(parent);
    }

    public static class StatementParser implements Parser<StatementToken> {

        public static final StatementParser instance = new StatementParser();

        private static final EitherParser parser = new EitherParser(STATEMENT_PARSERS);

        @Override
        public StatementToken __parse__(ParsableString string, Node parent) {
            Node node = parser.parse(string, parent);
            if (node == null) return null;
            return (StatementToken) node;
        }
    }
}

// statement
//    : blockLabel=block
//    | ASSERT expression (':' expression)? ';'
//    | IF parExpression statement (ELSE statement)?
//    | FOR '(' forControl ')' statement
//    | WHILE parExpression statement
//    | DO statement WHILE parExpression ';'
//    | TRY block (catchClause+ finallyBlock? | finallyBlock)
//    | TRY resourceSpecification block catchClause* finallyBlock?
//    | SWITCH parExpression '{' switchBlockStatementGroup* switchLabel* '}'
//    | SYNCHRONIZED parExpression block
//    | RETURN expression? ';'
//    | THROW expression ';'
//    | BREAK identifier? ';'
//    | CONTINUE identifier? ';'
//    | YIELD expression ';' // Java17
//    | SEMI
//    | statementExpression=expression ';'
//    | switchExpression ';'? // Java17
//    | identifierLabel=identifier ':' statement
//    ;