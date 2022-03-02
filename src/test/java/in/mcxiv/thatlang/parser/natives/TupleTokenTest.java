package in.mcxiv.thatlang.parser.natives;

import in.mcxiv.thatlang.parser.power.PowerUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static in.mcxiv.thatlang.parser.tokens.NameToken.NameParser.name;

class TupleTokenTest {
    @Test
    void test() {
        var parser = new TupleToken.TupleParser<>(name, PowerUtils.word("-"));
        var token = parser.parse("a-b-c - d- e - fg");
        Assertions.assertEquals("a", token.items.get(0).getValue());
        Assertions.assertEquals("b", token.items.get(1).getValue());
        Assertions.assertEquals("c", token.items.get(2).getValue());
        Assertions.assertEquals("d", token.items.get(3).getValue());
        Assertions.assertEquals("e", token.items.get(4).getValue());
        Assertions.assertEquals("fg", token.items.get(5).getValue());
    }
}