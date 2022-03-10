package in.mcxiv.thatlang.comments;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class MultilineCommentToken extends CommentToken {

    public MultilineCommentToken(String value) {
        super(value);
    }

    public MultilineCommentToken(Node parent, String value) {
        super(parent, value);
    }

    public static class MultilineCommentParser implements Parser<MultilineCommentToken> {

        public static final Parser<MultilineCommentToken> multiLineComment = new MultilineCommentParser();

        private MultilineCommentParser() {
        }

        @Override
        public MultilineCommentToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.matches(string, "/*"))
                return null;
            string.moveCursor(2);
            String everything = Cursors.fetchEverythingUpTo(string, "*/");
            if (everything == null) return null;
            string.moveCursor(2);
            return new MultilineCommentToken(parent, everything);
        }
    }
}
