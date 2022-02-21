package in.mcxiv.thatlang.parser.power;

import in.mcxiv.thatlang.parser.Parser;

public class PowerUtils {

    public static CompoundParser compound(Parser<?>... parsers) {
        return new CompoundParser(parsers);
    }

    public static CompoundParser repeatable(Parser<?>... parsers) {
        return new CompoundParser(parsers);
    }

}
