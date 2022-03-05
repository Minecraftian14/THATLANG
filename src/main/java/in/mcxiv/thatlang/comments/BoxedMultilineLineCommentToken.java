package in.mcxiv.thatlang.comments;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.utils.Cursors;

public class BoxedMultilineLineCommentToken extends CommentToken {

    public BoxedMultilineLineCommentToken(String value) {
        super(value);
    }

    public BoxedMultilineLineCommentToken(Node parent, String value) {
        super(parent, value);
    }

    public static class BoxedMultilineCommentParser implements Parser<BoxedMultilineLineCommentToken> {

        public static final Parser<BoxedMultilineLineCommentToken> boxedMultiLineComment = new BoxedMultilineCommentParser();

        private BoxedMultilineCommentParser() {
        }

        @Override
        public BoxedMultilineLineCommentToken __parse__(ParsableString string, Node parent) {
            if (Cursors.getCharAndNext(string) != '/')
                if (Cursors.bound(string)&&Cursors.getCharAndNext(string) != '*')
                    return null;
            String everything = Cursors.fetchEverythingUpTo(string, "*/");
            if(everything == null) return null;
            string.moveCursor(2);
            return new BoxedMultilineLineCommentToken(parent, everything);
        }
    }
}
