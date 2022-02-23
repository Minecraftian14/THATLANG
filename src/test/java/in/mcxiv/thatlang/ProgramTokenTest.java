package in.mcxiv.thatlang;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProgramTokenTest {
    @Test
    void test() {
        ProgramToken.ProgramParser parser = new ProgramToken.ProgramParser();

        assertNotNull(TestSuite.pj(parser.parse("""
                program aProgramName ->
                    aStatement.aMember
                """)));

        assertNotNull(TestSuite.pj(parser.parse("""
                program anotherProgramName:
                    aStatement.aMember
                    aStatement.aMember.anotherMember
                """)));
    }

    @Test
    void testSomething() {

    }
}