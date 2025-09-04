package com.nutrehogar.sistemacontable.ui.JComponents;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Llama al método de la clase padre para mantener el comportamiento básico
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Personalización de colores
        if (isSelected) {
            c.setBackground(Color.BLUE); // Fondo para celdas seleccionadas
            c.setForeground(Color.WHITE); // Texto para celdas seleccionadas
        } else {
            c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY); // Fondo alternado
            c.setForeground(Color.BLACK); // Texto por defecto
        }

        // Personalización de bordes
        ((JComponent) c).setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1), // Borde exterior
                BorderFactory.createEmptyBorder(5, 10, 5, 10) // Padding interno
        ));

        // Renderizado de contenido según el tipo de dato
        if (value instanceof Double) {
            setHorizontalAlignment(JLabel.RIGHT); // Alineación a la derecha para números
            setText(String.format("$%.2f", value)); // Formato de moneda
        } else if (value instanceof Boolean) {
            setHorizontalAlignment(JLabel.CENTER); // Alineación al centro para booleanos
            setText((Boolean) value ? "✔" : "✘"); // Representación visual de booleanos
        } else {
            setHorizontalAlignment(JLabel.LEFT); // Alineación a la izquierda por defecto
        }

        return c;
    }
}