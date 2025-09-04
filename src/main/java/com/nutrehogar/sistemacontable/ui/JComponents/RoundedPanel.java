package com.nutrehogar.sistemacontable.ui.JComponents;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

@Getter
@Setter
public class RoundedPanel extends JPanel {

    private int arcWidth;   // Ancho del borde redondeado
    private int arcHeight;  // Alto del borde redondeado
    private Color borderColor; // Color del borde
    private int borderThickness; // Grosor del borde
    private int borderOpacity; // Opacidad de la sombra

    {
        setOpaque(false);
    }

    public RoundedPanel(int arcWidth, int arcHeight, Color borderColor, int borderThickness) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
    }

    public RoundedPanel() {
        arcWidth = 30;
        arcHeight = 30;
        borderColor = Color.BLACK;
        borderThickness = 1;
        borderOpacity = 250;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Dibujar el fondo redondeado
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, width, height, arcWidth, arcHeight));

        // Dibujar el borde redondeado (si está configurado)
        if (borderColor != null && borderThickness > 0) {
            var color = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), borderOpacity);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(borderThickness));
            g2.draw(new RoundRectangle2D.Double(
                    borderThickness / 2.0, // Ajustar la posición para que el borde no se corte
                    borderThickness / 2.0,
                    width - borderThickness,
                    height - borderThickness,
                    arcWidth, arcHeight
            ));
            addBorderForBorderThickness(borderThickness);
        }

        g2.dispose();
    }

    private void addBorderForBorderThickness(int v) {
        this.setBorder(BorderFactory.createEmptyBorder(v, v, v, v));
    }
}