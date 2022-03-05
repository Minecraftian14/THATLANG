package in.mcxiv.thatlang.ui;

import in.mcxiv.thatlang.ui.layout.SolidLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EasyCanvas extends JPanel {

    ArrayList<Drawable> drawables = new ArrayList<>();

    public EasyCanvas() {
        setLayout(new SolidLayout());
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getForeground());
        for (Drawable drawable : drawables) drawable.draw(g2, this);
    }

    @Override
    public Component add(Component comp) {
        addImpl(comp, null, 0);
        return comp;
    }

    public void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }
}
