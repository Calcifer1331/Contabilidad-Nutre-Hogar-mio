package com.nutrehogar.sistemacontable.ui.JComponents;

import com.nutrehogar.sistemacontable.ui.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonPanel extends JButton {
    {
        setBorderPainted(false);
        setVerticalTextPosition(SwingConstants.TOP);
        setHorizontalAlignment(SwingConstants.LEADING);
        setMargin(new Insets(4, 6, 3, 14));
        setFont(ThemeConfig.Typography.FONT_LG);
        setIconTextGap(ThemeConfig.Spacing.GAP_MD);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.WHITE);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(ThemeConfig.Palette.COLUMBIA_BLUE);
            }
        });
    }

    public ButtonPanel() {
    }

    public ButtonPanel(String text) {
        super(text);
    }
}
