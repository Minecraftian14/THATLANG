package in.mcxiv.thatlang.ui.components;

import javax.swing.*;
import java.awt.*;

public class ShapePanel extends JPanel {

    public enum ShapeType {
        Rectangle
    }

    ShapeType shape;

    public ShapePanel(ShapeType shape) {
        this.shape = shape;
        setBackground(new Color(0x0, true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getForeground());
        switch (shape) {
            case Rectangle -> g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}