package com.nutrehogar.sistemacontable.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ThemeConfig {
    private ThemeConfig() {
        throw new IllegalStateException("Configure Class");
    }

    public static void setup() {
        System.setProperty("flatlaf.animation", "true");
        System.setProperty("flatlaf.menuBarEmbedded", "true");
        System.setProperty("flatlaf.useWindowDecorations", "true");
        UIManager.put("ScrollBar.width", 6); // Cambia el ancho
        UIManager.put("ScrollBar.thumbArc", 20); // Bordes redondeados
        UIManager.put("ScrollBar.thumb", Palette.SOLITUDE_200); // Color de la barra
        UIManager.put("ScrollBar.track", Color.WHITE); // Color del fondo
        UIManager.put("ScrollBar.trackArc", 7); // Bordes redondeados para el fondo
        UIManager.put("defaultFont", Typography.FONT_SM);
        UIManager.put("Table.background", Color.WHITE); // Fondo de la tabla
        UIManager.put("Table.foreground", Color.BLACK); // Color del textoa
        UIManager.put("Table.alternateRowColor", Palette.SOLITUDE_300); // Color de filas alternas (FlatLaf)
        UIManager.put("Table.selectionBackground", Palette.SOLITUDE_200); // Fondo de selección
        UIManager.put("Table.selectionForeground", Palette.SOLITUDE_600); // Texto de selección
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.arc", 10);
        UIManager.put("Button.focusedBorderColor", Palette.SOLITUDE_400);
        UIManager.put("Button.hoverBorderColor", Palette.SOLITUDE_300);
        UIManager.put("TextComponent.background", Color.WHITE);
        UIManager.put("TextComponent.foreground", Color.BLACK);
        UIManager.put("TextComponent.focusedBorderColor", Palette.SOLITUDE_400);
        UIManager.put("TextComponent.caretForeground", Palette.SOLITUDE_600);
        UIManager.put("TextComponent.arc", 7);

        UIManager.put("ComboBox.selectionBackground", Palette.SOLITUDE_200); // Color de fondo al seleccionar
        UIManager.put("ComboBox.buttonBackground", Color.WHITE); // Fondo del botón del desplegable
        UIManager.put("ComboBox.buttonHoverBackground", Palette.SOLITUDE_200); // Fondo del botón al pasar el mouse
        UIManager.put("ComboBox.arrowButtonBackground", Palette.SOLITUDE_200); // Fondo del botón de la flecha
        UIManager.put("ComboBox.arrowButtonHoverBackground", Palette.TRUE_BLUE); // Fondo de la flecha al pasar el mouse
        UIManager.put("ComboBox.arc", 7); // Esquinas redondeadas (solo en FlatLaf)
        UIManager.put("ComboBox.selectionBackground", Palette.SOLITUDE_200); // Color de fondo del item seleccionado
        UIManager.put("ComboBox.selectionForeground", Palette.SOLITUDE_700); // Color del texto del item seleccionado
        UIManager.put("ComboBox.rendererUseListColors", true); // Usa colores de la lista en los items
        UIManager.put("ComboBox.popup.arc", 7); // Redondeo de las esquinas

        FlatLightLaf.setup();
    }

    public static int scale(int value) {
        return UIScale.scale(value);
    }

    public static final int ICON_SM = scale(30);
    public static final int ICON_MD = scale(40);
    public static final int ICON_LG = scale(50);

    // 🔹 Configuración de Tipografías
    public static class Typography {
        private Typography() {
            throw new IllegalStateException("Configure Class");
        }

        public static final int TEXT_XS = scale(12);
        public static final int TEXT_SM = scale(14);
        public static final int TEXT_BASE = scale(16);
        public static final int TEXT_LG = scale(18);
        public static final int TEXT_XL = scale(20);

        public static final Font FONT_BASE = new Font("Roboto", Font.PLAIN, TEXT_BASE);
        public static final Font FONT_XS = FONT_BASE.deriveFont(Font.PLAIN, TEXT_XS);
        public static final Font FONT_SM = FONT_BASE.deriveFont(Font.PLAIN, TEXT_SM);
        public static final Font FONT_LG = FONT_BASE.deriveFont(Font.PLAIN, TEXT_LG);
        public static final Font FONT_XL = FONT_BASE.deriveFont(Font.PLAIN, TEXT_XL);

    }

    // 🔹 Espaciados globales (Padding, Margins, Gaps)
    public static class Spacing {
        private Spacing() {
            throw new IllegalStateException("Configure Class");
        }

        public static final int PADDING_SM = scale(4);
        public static final int PADDING_MD = scale(8);
        public static final int PADDING_LG = scale(16);

        public static final int MARGIN_SM = scale(4);
        public static final int MARGIN_MD = scale(8);
        public static final int MARGIN_LG = scale(16);

        public static final int GAP_SM = scale(4);
        public static final int GAP_MD = scale(8);
        public static final int GAP_LG = scale(16);

        public static final Border BORDER_MD = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    }

    // 🔹 Paleta de colores basada en Tailwind CSS
    public static class Palette {
        private Palette() {
            throw new IllegalStateException("Configure Class");
        }

        public static final Color TRANSPARENT = new Color(255, 255, 255, 0);
        public static final Color OFFICE_GREEN = new Color(24, 114, 18);
        public static final Color TRUE_BLUE = new Color(0, 102, 204);
        public static final Color ALICE_BLUE = new Color(229, 243, 255);
        public static final Color COLUMBIA_BLUE = new Color(214, 236, 255);
        public static final Color URANIAN_BLUE = new Color(178, 219, 255);
        public static final Color LIGHT_BLUE = new Color(146, 184, 215);
        public static final Color WHITE_SMOKE = new Color(242, 242, 242);
        public static final Color PLATINUM = new Color(220, 220, 220);

        public static final Color SOLITUDE_50 = new Color(239, 248, 255);
        public static final Color SOLITUDE_100 = new Color(229, 243, 255);
        public static final Color SOLITUDE_200 = new Color(184, 227, 255);
        public static final Color SOLITUDE_300 = new Color(121, 204, 255);
        public static final Color SOLITUDE_400 = new Color(50, 178, 254);
        public static final Color SOLITUDE_500 = new Color(7, 154, 240);
        public static final Color SOLITUDE_600 = new Color(0, 121, 206);
        public static final Color SOLITUDE_700 = new Color(0, 97, 166);
        public static final Color SOLITUDE_800 = new Color(3, 82, 137);
        public static final Color SOLITUDE_900 = new Color(9, 69, 113);
        public static final Color SOLITUDE_950 = new Color(6, 43, 75);

        // 🎨 Gris (Gray)
        public static final Color GRAY_50 = new Color(250, 250, 250);
        public static final Color GRAY_100 = new Color(245, 245, 245);
        public static final Color GRAY_200 = new Color(229, 229, 229);
        public static final Color GRAY_300 = new Color(212, 212, 212);
        public static final Color GRAY_400 = new Color(163, 163, 163);
        public static final Color GRAY_500 = new Color(115, 115, 115);
        public static final Color GRAY_600 = new Color(82, 82, 82);
        public static final Color GRAY_700 = new Color(64, 64, 64);
        public static final Color GRAY_800 = new Color(38, 38, 38);
        public static final Color GRAY_900 = new Color(23, 23, 23);
        public static final Color GRAY_950 = new Color(12, 12, 12);
    }
}
