package com.nutrehogar.sistemacontable.ui.JComponents;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    /**
     * Carga una fuente desde un archivo.
     *
     * <p>Como usar: {@code Font customFont = FontLoader.loadFont("/fonts/MiFuente.ttf", 24f);}
     *
     * @param path Ruta relativa al archivo de la fuente (por ejemplo, "/fonts/MiFuente.ttf").
     * @param size Tamaño de la fuente.
     * @return La fuente cargada, o una fuente por defecto si hay un error.
     */
    public static @NotNull Font loadFont(String path, float size) {
        try {
            // Cargar la fuente desde el archivo
            InputStream fontStream = FontLoader.class.getResourceAsStream(path);
            if (fontStream == null) {
                throw new IOException("No se pudo encontrar el archivo de la fuente: " + path);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            return font.deriveFont(size); // Ajustar el tamaño de la fuente
        } catch (FontFormatException | IOException e) {
            System.err.println("Error al cargar la fuente: " + e.getMessage());
            return new Font("Arial", Font.PLAIN, (int) size); // Fuente por defecto en caso de error
        }
    }
}