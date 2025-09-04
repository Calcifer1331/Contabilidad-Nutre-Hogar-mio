package com.nutrehogar.sistemacontable.ui.components;

import lombok.Setter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

@Setter
public class DocumentSizeFilter extends DocumentFilter {
    private int maxCaracteres; // Límite de 10 caracteres

    public DocumentSizeFilter(int maxCaracteres) {
        this.maxCaracteres = maxCaracteres;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string.matches("\\d*") && (fb.getDocument().getLength() + string.length()) <= maxCaracteres) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text.matches("\\d*") && (fb.getDocument().getLength() + text.length() - length) <= maxCaracteres) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
