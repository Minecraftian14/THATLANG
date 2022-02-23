package in.mcxiv.thatlang;

import in.mcxiv.TestSuite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProgramFileTokenTest {
    @Test
    void test() {
        ProgramFileToken.ProgramFileParser parser = new ProgramFileToken.ProgramFileParser();
        Assertions.assertNotNull(TestSuite.pj(parser.parse("""
                
                program aProgramName:
                    aStatement.aMember
                    
                program anotherProgramName:
                    aStatement.aMember
                    aStatement.aMember.anotherMember
                    
                """)));

    }
}