package in.mcxiv.thatlang.statements;

import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.parser.power.EitherParser;
import in.mcxiv.parser.Node;
import in.mcxiv.thatlang.comments.CommentToken;
import in.mcxiv.thatlang.comments.CommentToken.CommentParser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.statements.AssignmentToken.AssignmentParser;
import in.mcxiv.thatlang.statements.ElseIfStatementToken.ElseIfStatementParser;
import in.mcxiv.thatlang.statements.ElseStatementToken.ElseStatementParser;
import in.mcxiv.thatlang.statements.ForEachToken.ForEachParser;
import in.mcxiv.thatlang.statements.ForStatementToken.ForStatementParser;
import in.mcxiv.thatlang.statements.IfStatementToken.IfStatementParser;
import in.mcxiv.thatlang.statements.MultiAssignmentToken.MultiAssignmentParser;
import in.mcxiv.interpreter.Interpretable;
import thatlang.core.THATObject;
import thatlang.core.THOSEObjects;

import java.util.ArrayList;
import java.util.List;

import static in.mcxiv.thatlang.statements.QuantaStatement.QuantaStatementParser;
import static in.mcxiv.thatlang.statements.VariableDefinitionToken.VariableDefinitionParser;

public class StatementToken extends Node implements Interpretable<AbstractVM, THATObject> {

    public static final ArrayList<Parser<?>> STATEMENT_PARSERS = new ArrayList<>(List.of(
            VariableDefinitionParser.variableDef,
            AssignmentParser.assignment,
            MultiAssignmentParser.multiAssignment,
            ForStatementParser.forStatement,
            ForEachParser.forEachStatement,
            IfStatementParser.ifStatement,
            ElseIfStatementParser.elseIfStatement,
            ElseStatementParser.elseStatement,
            IfStatementParser.ifStatement,
            BinaryFunctionStatementToken.binaryFunctionStmt,
            QuantaStatementParser.quantaStatement,
            CommentParser.comment
    ));

    public static final ArrayList<Class<?>> STATEMENT_TYPES = new ArrayList<>(List.of(
            VariableDefinitionToken.class,
            AssignmentToken.class,
            MultiAssignmentToken.class,
            ForStatementToken.class,
            ForEachToken.class,
            IfStatementToken.class,
            ElseIfStatementToken.class,
            ElseStatementToken.class,
            QuantaStatement.class,
            CommentToken.class
    ));

    private final boolean isCondensable;
    private final Class<?>[] condensability;

    public StatementToken() {
        this((Node) null);
    }

    public StatementToken(Node parent) {
        this(parent, new Class[0]);
    }

    public StatementToken(Class<?>... condensability) {
        this(null, condensability);
    }

    public StatementToken(Node parent, Class<?>... condensability) {
        super(parent);
        this.isCondensable = condensability.length != 0;
        this.condensability = condensability;
    }

    public boolean isCondensable() {
        return isCondensable;
    }

    public boolean isAccepted(StatementToken token) {
        for (Class<?> clazz : condensability)
            if (clazz.isInstance(token))
                return true;
        return false;
    }

    public void processCondensability(StatementToken token) {
    }

    @Override
    public THATObject interpret(AbstractVM vm) {
        return THOSEObjects.NULL;
    }

    public static class StatementParser implements Parser<StatementToken> {

        public static final StatementParser statement = new StatementParser();

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