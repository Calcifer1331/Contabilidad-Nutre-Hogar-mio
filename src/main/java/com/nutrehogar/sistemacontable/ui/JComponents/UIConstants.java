package com.nutrehogar.sistemacontable.ui.JComponents;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class UIConstants {
    private UIConstants() {
        throw new AssertionError("This class cannot be instantiated");
    }

    // Cargar SVG desde la ruta del classpath
    public static final FlatSVGIcon ICON_MAYOR_GENERAL = new FlatSVGIcon("svgs/RiBankLine.svg");
    // Fuentes
    public static final Font FONT = new Font("Segoe UI Semibold", Font.PLAIN, 14);
    public static final Font FONT_TITLE = FONT.deriveFont(Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = FONT.deriveFont(Font.BOLD, 18);
    public static final Font FONT_NORMAL = FONT.deriveFont(Font.PLAIN, 14);
    public static final Font FONT_SMALL = FONT.deriveFont(Font.PLAIN, 12);

    // Colores
    public static final Color COLOR_FONT = new Color(39, 41, 39);
    public static final Color COLOR_PRIMARY = new Color(242, 242, 242); // Azul
    public static final Color COLOR_SECONDARY = new Color(48, 48, 48); // Gris claro
    public static final Color COLOR_SUCCESS = new Color(248, 245, 241); // Verde
    public static final Color COLOR_DANGER = new Color(220, 53, 69); // Rojo
    public static final Color COLOR_WARNING = new Color(255, 193, 7); // Amarillo
    public static final Color COLOR_INFO = new Color(23, 162, 184); // Azul claro

    // Tamaños y márgenes
    public static final int PADDING_SMALL = 5;
    public static final int PADDING_MEDIUM = 10;
    public static final int PADDING_LARGE = 20;

    // Bordes
    public static final Border BORDER_TEXT_FIELD = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(PADDING_SMALL, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL)
    );

    // Otros
    public static final int TABLE_ROW_HEIGHT = 25;

}
