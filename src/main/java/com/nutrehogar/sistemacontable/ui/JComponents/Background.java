package com.nutrehogar.sistemacontable.ui.JComponents;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

@Getter
@Setter
public class Background extends JPanel {

    private int radioIncremento;
    private PositionCentral centerPosition;
    private FundType fundType;
    private NumberOfColors numberOfColors;
    private Point2D.Double center;
    private Point2D.Double startPoint;
    private Point2D.Double endPoint;
    private Color color1;
    private Color color2;
    private Color color3;
    private Color color4;
    private float fraction1;
    private float fraction2;
    private float fraction3;
    private float fraction4;

    public Background() {
        loadDefaultConfig();
    }

    /**
     * Carga la configuración por defecto del fondo.
     */
    private void loadDefaultConfig() {
        setColors(hexToColor("#26D0CE"), hexToColor("#1A2980"), hexToColor("#0D1540"), hexToColor("#000000"));
        setRadioIncremento(100);
        setCenterPosition(PositionCentral.DOWN);
        setFractions(0.0f, 0.3f, 0.6f, 1.0f);
        setFundType(FundType.RADIAL);
        setNumberOfColors(NumberOfColors.TREE);
    }

    /**
     * Establece las fracciones para los colores del degradado.
     *
     * @param fractions Fracciónes. (4)
     */
    public void setFractions(float @NotNull ... fractions) {
        this.fraction1 = fractions[0];
        this.fraction2 = fractions[1];
        this.fraction3 = fractions[2];
        this.fraction4 = fractions[3];
    }

    /**
     * Establece los colores para el degradado.
     *
     * @param colors Colores (5).
     */
    public void setColors(Color @NotNull ... colors) {
        this.color1 = colors[0];
        this.color2 = colors[1];
        this.color3 = colors[2];
        this.color4 = colors[3];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        double width = getWidth();
        double height = getHeight();

        // Configurar colores y fracciones según el número de colores
        Color[] colors = getColors();
        float[] fractions = getFractions();

        // Configurar puntos de inicio y fin según la posición central
        configureGradientPoints(width, height);

        // Aplicar el degradado según el tipo de fondo
        Paint paint = createGradientPaint(width, height, colors, fractions);
        g2.setPaint(paint);

        // Pintar el fondo
        g2.fillRect(0, 0, (int) width, (int) height);
    }

    /**
     * Configura los puntos de inicio y fin del degradado según la posición central.
     *
     * @param width  Ancho del panel.
     * @param height Alto del panel.
     */
    private void configureGradientPoints(double width, double height) {
        switch (centerPosition) {
            case UP -> {
                center = new Point2D.Double(width / 2, 0);
                startPoint = new Point2D.Double(width / 2, 0);
                endPoint = new Point2D.Double(width / 2, height);
            }
            case DOWN -> {
                center = new Point2D.Double(width / 2, height);
                startPoint = new Point2D.Double(width / 2, height);
                endPoint = new Point2D.Double(width / 2, 0);
            }
            case CENTER -> {
                fundType = FundType.RADIAL;
                center = new Point2D.Double(width / 2, height / 2);
                startPoint = new Point2D.Double(width / 2, 0);
                endPoint = new Point2D.Double(width / 2, height);
            }
            case RIGHT -> {
                center = new Point2D.Double(0, height / 2);
                startPoint = new Point2D.Double(width, height / 2);
                endPoint = new Point2D.Double(0, height / 2);
            }
            case LEFT -> {
                center = new Point2D.Double(width, height / 2);
                startPoint = new Point2D.Double(0, height / 2);
                endPoint = new Point2D.Double(width, height / 2);
            }
            case LEFT_UP -> {
                center = new Point2D.Double(0, 0);
                startPoint = new Point2D.Double(0, 0);
                endPoint = new Point2D.Double(width, height);
            }
            case LEFT_DOWN -> {
                center = new Point2D.Double(0, height);
                startPoint = new Point2D.Double(0, height);
                endPoint = new Point2D.Double(width, 0);
            }
            case RIGHT_DOWN -> {
                center = new Point2D.Double(width, height);
                startPoint = new Point2D.Double(width, height);
                endPoint = new Point2D.Double(0, 0);
            }
            case RIGHT_UP -> {
                center = new Point2D.Double(width, 0);
                startPoint = new Point2D.Double(width, 0);
                endPoint = new Point2D.Double(0, height);
            }
        }
    }

    /**
     * Obtiene los colores según el número de colores configurado.
     *
     * @return Arreglo de colores.
     */
    @Contract(value = " -> new", pure = true)
    private Color @NotNull [] getColors() {
        return switch (numberOfColors) {
            case ONE -> {
                fundType = FundType.SOLID;
                yield new Color[]{color1};
            }
            case TWO -> new Color[]{color1, color2};
            case TREE -> new Color[]{color1, color2, color3};
            case FOUR -> new Color[]{color1, color2, color3, color4};
        };
    }

    /**
     * Obtiene las fracciones según el número de colores configurado.
     *
     * @return Arreglo de fracciones.
     */
    @Contract(pure = true)
    private float @NotNull [] getFractions() {
        return switch (numberOfColors) {
            case ONE -> new float[]{fraction1};
            case TWO -> new float[]{fraction1, fraction2};
            case TREE -> new float[]{fraction1, fraction2, fraction3};
            default -> new float[]{fraction1, fraction2, fraction3, fraction4};
        };
    }

    /**
     * Crea el degradado según el tipo de fondo.
     *
     * @param width     Ancho del panel.
     * @param height    Alto del panel.
     * @param colors    Colores del degradado.
     * @param fractions Fracciones del degradado.
     * @return El degradado configurado.
     */
    private Paint createGradientPaint(double width, double height, Color[] colors, float[] fractions) {
        float radio = (float) (Math.min(width, height) + radioIncremento);

        return switch (fundType) {
            case RADIAL -> new RadialGradientPaint(center, radio, fractions, colors);
            case LINEAL -> new LinearGradientPaint(startPoint, endPoint, fractions, colors);
            default -> color1;
        };
    }

    /**
     * Convierte un código hexadecimal a un objeto Color.
     *
     * @param hex Código hexadecimal del color.
     * @return Objeto Color.
     */
    public Color hexToColor(String hex) {
        try {
            return Color.decode(hex);
        } catch (NumberFormatException e) {
            return Color.BLACK; // Valor predeterminado en caso de error
        }
    }

    public enum PositionCentral {
        UP, CENTER, RIGHT, LEFT, DOWN, LEFT_DOWN, LEFT_UP, RIGHT_DOWN, RIGHT_UP
    }

    public enum FundType {
        RADIAL, LINEAL, SOLID
    }

    public enum NumberOfColors {
        ONE, TWO, TREE, FOUR;
        private int value;
    }
}