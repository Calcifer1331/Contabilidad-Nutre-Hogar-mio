package com.nutrehogar.sistemacontable.ui.JComponents;

import lombok.Getter;
import lombok.Setter;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
@Getter
@Setter
public class ShadowBorder extends AbstractBorder {

    private int shadowSize; // Tamaño de la sombra
    private int arcWidth;   // Ancho del borde redondeado de la sombra
    private int arcHeight;  // Alto del borde redondeado de la sombra
    private Color shadowColor; // Color de la sombra
    private int shadowOpacity; // Opacidad de la sombra

    public ShadowBorder(int shadowSize, int arcWidth, int arcHeight, Color shadowColor, int shadowOpacity) {
        this.shadowSize = shadowSize;
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.shadowColor = shadowColor;
        this.shadowOpacity = shadowOpacity;
    }

    public ShadowBorder() {
        shadowSize = 10;
        arcWidth = 10;
        arcHeight = 10;
        shadowColor = Color.BLACK;
        shadowOpacity = 10;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar la sombra
        if (shadowSize > 0 && shadowOpacity > 0) {
            Color shadow = new Color(shadowColor.getRed(), shadowColor.getGreen(), shadowColor.getBlue(), shadowOpacity);
            g2.setColor(shadow);

            for (int i = 0; i < shadowSize; i++) {
                float opacity = (float) (shadowSize - i) / shadowSize;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity * 0.5f));

                g2.fill(new RoundRectangle2D.Double(
                        x + i, y + i, width - 2 * i, height - 2 * i, arcWidth, arcHeight
                ));
            }
        }

        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}