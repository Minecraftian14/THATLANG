package in.mcxiv.thatlang.comments;

import in.mcxiv.parser.Node;
import in.mcxiv.parser.ParsableString;
import in.mcxiv.parser.Parser;
import in.mcxiv.thatlang.interpreter.AbstractVM;
import in.mcxiv.utils.Cursors;
import thatlang.core.THATObject;

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
            if (Cursors.getCharAndNext(string) != '/')
                if (Cursors.bound(string)&&Cursors.getCharAndNext(string) != '/')
                    return null;
            String everything = Cursors.fetchEverythingUpTo(string, "\n");
            if(everything == null) return null;
            return new SingleLineCommentToken(parent, everything);
        }
    }
}
