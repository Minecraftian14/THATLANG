package in.mcxiv.refl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.swing.*;

class ClazzonicTest {

    @Test
    @Disabled("We are no longer using reflection magic.")
    void testSimpleMethod() {
        Class<JPanel> clazz = Clazzonic.getClazz();
        Assertions.assertTrue(JComponent.class.isAssignableFrom(JPanel.class));
        Assertions.assertTrue(JComponent.class.isAssignableFrom(clazz));
        System.out.println(clazz);
    }
}