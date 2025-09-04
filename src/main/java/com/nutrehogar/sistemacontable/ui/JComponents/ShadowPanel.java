package com.nutrehogar.sistemacontable.ui.JComponents;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class ShadowPanel extends JPanel {

    private int shadowSize; // Tamaño de la sombra
    private Color shadowColor; // Color de la sombra
    private boolean shadowTop;    // Sombra en la parte superior
    private boolean shadowBottom; // Sombra en la parte inferior
    private boolean shadowLeft;   // Sombra en la parte izquierda
    private boolean shadowRight;  // Sombra en la parte derecha
    private int shadowOpacity;

    private Color borderColor; // Color del borde
    private int borderThickness; // Grosor del borde
    private int borderOpacity;   // Opacidad del borde (0-255)

    // Redondeo de las esquinas del panel visible
    private int topLeftArcVisible = 20;
    private int topRightArcVisible = 20;
    private int bottomLeftArcVisible = 20;
    private int bottomRightArcVisible = 20;

    // Redondeo de las esquinas de la sombra
    private int topLeftArcShadow = 20;
    private int topRightArcShadow = 20;
    private int bottomLeftArcShadow = 20;
    private int bottomRightArcShadow = 20;

    public ShadowPanel() {
        setOpaque(false); // Hacer el panel transparente
        setPreferredSize(new Dimension(300, 200));
        setBackground(Color.WHITE);

        // Configuración por defecto
        shadowBottom = true;
        shadowLeft = true;
        shadowRight = true;
        shadowTop = true;
        setShadowOpacity(50); // Opacidad de la sombra
        shadowSize = 10; // Tamaño de la sombra

        // Configuración por defecto del borde
        borderColor = Color.BLACK; // Color del borde por defecto
        borderThickness = 1; // Grosor del borde por defecto
        borderOpacity = 255; // Opacidad del borde por defecto (totalmente opaco)
    }

    public void setShadowOpacity(int opaque) {
        if (opaque > 255) opaque = 255;
        if (opaque < 0) opaque = 0;
        shadowOpacity = opaque;
        shadowColor = new Color(0, 0, 0, shadowOpacity);
    }

    public void setBorderOpacity(int opaque) {
        if (opaque > 255) opaque = 255;
        if (opaque < 0) opaque = 0;
        borderOpacity = opaque;
        repaint(); // Redibujar el panel cuando se cambia la opacidad del borde
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Dibujar la sombra según la configuración
        if (shadowTop || shadowBottom || shadowLeft || shadowRight && shadowSize > 0 && shadowOpacity > 0) {
            g2.setColor(shadowColor);
            for (int i = 0; i < shadowSize; i++) {
                float opacity = (float) (shadowSize - i) / shadowSize;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity * 0.5f));

                // Calcular las coordenadas de la sombra
                int x = shadowLeft ? i : 0;
                int y = shadowTop ? i : 0;
                int w = width - (shadowLeft ? i : 0) - (shadowRight ? i : 0);
                int h = height - (shadowTop ? i : 0) - (shadowBottom ? i : 0);

                // Dibujar la sombra con esquinas personalizadas
                drawCustomRoundedRect(g2, x, y, w, h, topLeftArcShadow, topRightArcShadow, bottomLeftArcShadow, bottomRightArcShadow);
            }
            addBorderForShadow();
        }

        // Dibujar el panel principal
        g2.setComposite(AlphaComposite.SrcOver); // Restaurar la opacidad
        g2.setColor(getBackground());
        drawCustomRoundedRect(g2, shadowLeft ? shadowSize : 0, shadowTop ? shadowSize : 0, width - (shadowLeft ? shadowSize : 0) - (shadowRight ? shadowSize : 0), height - (shadowTop ? shadowSize : 0) - (shadowBottom ? shadowSize : 0), topLeftArcVisible, topRightArcVisible, bottomLeftArcVisible, bottomRightArcVisible);

        // Dibujar el borde (si está configurado)
        if (borderColor != null && borderThickness > 0 && borderOpacity > 0) {
            g2.setColor(new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), borderOpacity));
            g2.setStroke(new BasicStroke(borderThickness));
            drawCustomRoundedRect(g2, (shadowLeft ? shadowSize : 0) + borderThickness / 2.0, // Ajustar la posición para que el borde no se corte
                    (shadowTop ? shadowSize : 0) + borderThickness / 2.0, width - (shadowLeft ? shadowSize : 0) - (shadowRight ? shadowSize : 0) - borderThickness, height - (shadowTop ? shadowSize : 0) - (shadowBottom ? shadowSize : 0) - borderThickness, topLeftArcVisible, topRightArcVisible, bottomLeftArcVisible, bottomRightArcVisible);
        }

        g2.dispose();
    }

    private void addBorderForShadow() {
        this.setBorder(BorderFactory.createEmptyBorder(shadowTop ? shadowSize + borderThickness : 0, shadowLeft ? shadowSize + borderThickness : 0, shadowBottom ? shadowSize : 0, shadowRight ? shadowSize : 0));
    }

    /**
     * Dibuja un rectángulo redondeado con esquinas personalizadas.
     *
     * @param g2             Objeto Graphics2D.
     * @param x              Coordenada X.
     * @param y              Coordenada Y.
     * @param width          Ancho del rectángulo.
     * @param height         Alto del rectángulo.
     * @param topLeftArc     Radio de la esquina superior izquierda.
     * @param topRightArc    Radio de la esquina superior derecha.
     * @param bottomLeftArc  Radio de la esquina inferior izquierda.
     * @param bottomRightArc Radio de la esquina inferior derecha.
     */
    private void drawCustomRoundedRect(Graphics2D g2, double x, double y, double width, double height, int topLeftArc, int topRightArc, int bottomLeftArc, int bottomRightArc) {
        // Crear un camino (Path2D) para el rectángulo redondeado
        java.awt.geom.Path2D.Double path = new java.awt.geom.Path2D.Double();

        // Esquina superior izquierda
        path.moveTo(x + topLeftArc, y);
        path.quadTo(x, y, x, y + topLeftArc);

        // Esquina inferior izquierda
        path.lineTo(x, y + height - bottomLeftArc);
        path.quadTo(x, y + height, x + bottomLeftArc, y + height);

        // Esquina inferior derecha
        path.lineTo(x + width - bottomRightArc, y + height);
        path.quadTo(x + width, y + height, x + width, y + height - bottomRightArc);

        // Esquina superior derecha
        path.lineTo(x + width, y + topRightArc);
        path.quadTo(x + width, y, x + width - topRightArc, y);

        // Cerrar el camino
        path.closePath();

        // Dibujar el rectángulo redondeado
        g2.fill(path);
    }

    // Métodos adicionales para personalización
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint(); // Redibujar el panel cuando se cambia el color del borde
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        repaint(); // Redibujar el panel cuando se cambia el grosor del borde
    }
}