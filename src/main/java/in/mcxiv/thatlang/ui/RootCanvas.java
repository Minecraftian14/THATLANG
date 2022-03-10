package in.mcxiv.thatlang.ui;

import javax.swing.*;
import java.awt.*;

public class RootCanvas extends JFrame {

    public final EasyCanvas canvas;

    public RootCanvas(String title) throws HeadlessException {
        this(title, 500);
    }

    public RootCanvas(String title, int size) throws HeadlessException {
        this(title, size, size);
    }

    public RootCanvas(String title, int width, int height) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        canvas = new EasyCanvas();
        setSize(width, height);
    }

    @Override
    public void setSize(int width, int height) {
        remove(canvas);
        super.setSize(width, height);
        add(canvas, new GridBagConstraints() {{
            weightx = weighty = 1;
            ipadx = width;
            ipady = height;
        }});
//        canvas.setBounds(0, 0, width, height);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
