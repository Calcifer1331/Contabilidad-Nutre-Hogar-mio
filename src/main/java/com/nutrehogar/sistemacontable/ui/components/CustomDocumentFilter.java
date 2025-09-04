package com.nutrehogar.sistemacontable.ui.components;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class CustomDocumentFilter extends DocumentFilter {
    private final Type type;

    public CustomDocumentFilter(Type type) {
        super();
        this.type = type;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);

        if (type.isValid(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder();
        sb.append(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);

        if (type.isValid(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    public enum Type {
        INTEGER {
            @Override
            protected boolean isValid(String text) {
                return text.matches("\\d*");
            }
        }, DECIMAL {
            @Override
            protected boolean isValid(String text) {
                return text.matches("^\\d*\\.?\\d*$");
            }
        };

        protected abstract boolean isValid(String text);
    }
}