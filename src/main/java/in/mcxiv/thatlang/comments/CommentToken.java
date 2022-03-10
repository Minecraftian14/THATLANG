package in.mcxiv.thatlang.comments;

import in.mcxiv.interpreter.Interpretable;
import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.thatlang.statements.StatementToken;
import thatlang.core.THATObject;

import static in.mcxiv.parser.power.PowerUtils.either;
import static in.mcxiv.thatlang.comments.BoxedMultilineLineCommentToken.BoxedMultilineCommentParser.boxedMultiLineComment;
import static in.mcxiv.thatlang.comments.MultilineCommentToken.MultilineCommentParser.multiLineComment;
import static in.mcxiv.thatlang.comments.SingleLineCommentToken.SingleLineCommentParser.singleLineComment;
import static in.mcxiv.thatlang.comments.TableCommentToken.TableCommentParser.tableComment;

public class CommentToken extends StatementToken implements Interpretable<AbstractVM, THATObject> {

    private String content;

    public CommentToken(String value) {
        this(null, value);
    }

    public CommentToken(Node parent, String value) {
        super(parent);
        content = value;
    }

    @Override
    public String toString() {
        return toExtendedString("content", content);
    }

    public static class CommentParser implements Parser<CommentToken> {

        public static final Parser<CommentToken> comment = new CommentParser();

        private static final Parser<?> parser = either(singleLineComment, boxedMultiLineComment, multiLineComment, tableComment);

        private CommentParser() {
        }

        @Override
        public CommentToken __parse__(ParsableString string, Node parent) {
            return (CommentToken) parser.parse(string, parent);
        }
    }
}