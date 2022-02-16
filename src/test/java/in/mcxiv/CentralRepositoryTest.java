package in.mcxiv;

import org.junit.jupiter.api.Test;

import static in.mcxiv.TestSuite.alsoPrtln;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CentralRepositoryTest {

    @Test
    void testSimpleMethods() {
        assertEquals("CENTRAL_REPOSITORY_TEST", alsoPrtln(CentralRepository.getLogHead(CentralRepositoryTest.class)));
        assertEquals("CENTRAL_REPOSITORY_TEST", alsoPrtln(CentralRepository.getLogHead()));
    }
}