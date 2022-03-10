package in.mcxiv.thatlang.comments;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class SingleLineCommentToken extends CommentToken {

    public SingleLineCommentToken(String value) {
        super(value);
    }

    public SingleLineCommentToken(Node parent, String value) {
        super(parent, value);
    }

    public static class SingleLineCommentParser implements Parser<SingleLineCommentToken> {

        public static final Parser<SingleLineCommentToken> singleLineComment = new SingleLineCommentParser();

        private SingleLineCommentParser() {
        }

        @Override
        public SingleLineCommentToken __parse__(ParsableString string, Node parent) {
            if (!Cursors.matches(string, "//"))
                return null;
            string.moveCursor(2);
            String everything = Cursors.fetchEverythingUpTo(string, "\n");
            if (everything == null) return null;
            return new SingleLineCommentToken(parent, everything);
        }
    }
}
