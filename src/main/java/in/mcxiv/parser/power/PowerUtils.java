package in.mcxiv.parser.power;

import in.mcxiv.parser.Parser;

import java.util.List;

public class PowerUtils {

    public static CompoundParser compound(Parser<?>... parsers) {
        return new CompoundParser(parsers);
    }

    public static CompoundParser compound(List<Parser<?>> parsers) {
        return new CompoundParser(parsers);
    }

    public static EitherParser either(Parser<?>... parsers) {
        return new EitherParser(parsers);
    }

    public static EitherParser either(List<Parser<?>> parsers) {
        return new EitherParser(parsers);
    }

    public static LooseBlockParser block(Parser<?> parser) {
        return new LooseBlockParser(parser);
    }

    public static LooseInlineParser inline(Parser<?> parser) {
        return new LooseInlineParser(parser);
    }

    public static LooseInlineParser inline(String word) {
        return new LooseInlineParser(word);
    }

    public static RepeatableParser repeatable(Parser<?> parser) {
        return new RepeatableParser(parser);
    }

    public static RepeatableParser repeatable(int minimum, Parser<?> parser) {
        return new MinimumRepeatableParser(minimum, parser);
    }

    public static OptionalParser optional(Parser<?> parser) {
        return new OptionalParser(parser);
    }

    public static WordParser word(String word) {
        return new WordParser(word);
    }

}
