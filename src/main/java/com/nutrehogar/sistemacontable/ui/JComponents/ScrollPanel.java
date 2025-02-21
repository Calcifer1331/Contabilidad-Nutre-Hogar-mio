
package com.nutrehogar.sistemacontable.ui.JComponents;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class ScrollPanel extends JScrollPane {
    {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        getViewport().setOpaque(false);
        setHorizontalScrollBar(null);
        setBorder(null);
    }

    public ScrollPanel() {
    }

    public ScrollPanel(Component view) {
        super(view);
    }
}
