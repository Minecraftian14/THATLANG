package in.mcxiv.thatlang;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProgramFileTokenTest {
    @Test
    void test() {
        ProgramFileToken.ProgramFileParser parser = new ProgramFileToken.ProgramFileParser();
        Assertions.assertNotNull(TestSuite.pj(parser.parse("""
                
                aProgramName ->
                    aStatement.aMember
                    
                anotherProgramName ->
                    aStatement.aMember
                    aStatement.aMember.anotherMember
                    
                """)));

    }
}